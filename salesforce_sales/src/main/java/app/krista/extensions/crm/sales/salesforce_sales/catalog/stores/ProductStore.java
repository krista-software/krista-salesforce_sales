/*
 * Salesforce Sales Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.QueryClause;
import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.ProductCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Product;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ProductDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ProductImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFProduct;
import app.krista.extensions.crm.sales.salesforce_sales.util.SoqlQueryHelper;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static app.krista.extensions.crm.sales.salesforce_sales.impl.ProductDaoImpl.QUERY_FIELDS;

/**
 * EntityStore implementation for Product entity.
 * Provides CRUD operations for products.
 */
@Service
public class ProductStore implements EntityStore<Product> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStore.class);

    private final ProductDaoImpl productDao;

    @Inject
    public ProductStore(ProductDaoImpl productDao) {
        this.productDao = productDao;
    }

    @Override
    public Product create(Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()) {
            LOGGER.info("Entity attribute field map cannot be null or empty");
            return new Product();
        }
        try {
            String productId = productDao.addProduct(fields);
            return get(productId);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return new Product();
        }
    }

    @Override
    public Product get(String productId) {
        if (productId == null || productId.isEmpty()) {
            LOGGER.info("Primary key cannot be null or empty");
            return null;
        }
        try {
            return ProductCatalogType.fromSFProduct(productDao.getProductById(productId));
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public Product update(Product product) {
        if (product == null) {
            LOGGER.info("Product cannot be null");
            return null;
        }
        try {
            productDao.updateProduct(new ProductImpl(SFProduct.from(product)));
            return get(product.id);
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return null;
        }
    }

    @Override
    public void delete(String productId) {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("Product ID is required for deletion. Please provide the Salesforce ID of the product you want to delete.");
        }
        productDao.deleteProduct(productId);
    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public List<Product> search(List<SearchCondition> conditions, long pageIndex, int pageSize) {
        return List.of();
    }

    @Override
    public List<Product> search(SearchQuery searchQuery, long pageIndex, int pageSize) throws IOException {
        if (searchQuery == null || searchQuery.getSearchConditions() == null || searchQuery.getSearchConditions().isEmpty()) {
            return productDao.getProducts(pageSize, pageIndex).stream().map(ProductCatalogType::fromSFProduct).collect(Collectors.toList());
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        List<QueryClause> clauses = searchQuery.getQueryClauses();
        try {
            // Single Id lookup optimization
            for (SearchCondition sc : conditions) {
                if ("Id".equalsIgnoreCase(sc.getFieldName()) && sc.getOperand() != null) {
                    String id = String.valueOf(sc.getOperand()).trim();
                    if (!id.isEmpty()) {
                        Product entity = get(id);
                        return entity != null ? List.of(entity) : List.of();
                    }
                }
            }
            String where = SoqlQueryHelper.buildWhereClause("product", conditions);
            String orderBy = SoqlQueryHelper.buildOrderByClause("product", clauses);
            String pagination = SoqlQueryHelper.buildPaginationClause(pageSize, pageIndex);

            if (where.isEmpty()) {
                return productDao.getProducts(pageSize, pageIndex).stream().map(ProductCatalogType::fromSFProduct).collect(Collectors.toList());
            }
            String query = "SELECT " + QUERY_FIELDS + " FROM Product2 WHERE " + where + orderBy + pagination;
            return productDao.getProductsByQuery(query).stream().map(ProductCatalogType::fromSFProduct).collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public long count(List<SearchCondition> list) {
        return 0;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) {
        return List.of();
    }
}

