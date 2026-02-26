package app.krista.extensions.crm.sales.salesforce_sales.catalog.stores;

import app.krista.extension.executor.SearchCondition;
import app.krista.extension.executor.SearchQuery;
import app.krista.extension.util.EntityStore;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.catalogtypes.ProductCatalogType;
import app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Product;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ProductDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.ProductImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFProduct;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            LOGGER.info("Search conditions are null or empty, returning all products");
            try {
                return productDao.getProducts(Math.max(pageSize, 200), pageIndex).stream()
                        .map(ProductCatalogType::fromSFProduct)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException cause) {
                LOGGER.error(cause.getMessage(), cause);
                return List.of();
            }
        }
        List<SearchCondition> conditions = searchQuery.getSearchConditions();
        try {
            List<Product> products = new ArrayList<>();
            boolean hasNonEmptyCondition = false;
            for (SearchCondition condition : conditions) {
                String matchPattern = condition.getOperand() != null ? String.valueOf(condition.getOperand()).trim() : "";
                String selector = condition.getFieldName();

                if (matchPattern.isEmpty()) {
                    continue;
                }

                if ("Product Code".equalsIgnoreCase(selector)) {
                    hasNonEmptyCondition = true;
                    app.krista.extensions.crm.sales.salesforce_sales.service.Product product = productDao.getProductByProductCode(matchPattern);
                    if (product != null) {
                        products.add(ProductCatalogType.fromSFProduct(product));
                    }
                } else if ("Family".equalsIgnoreCase(selector)) {
                    hasNonEmptyCondition = true;
                    products = productDao.getProductsByFamily(matchPattern, Math.max(pageSize, 200), pageIndex).stream()
                            .map(ProductCatalogType::fromSFProduct)
                            .collect(Collectors.toList());
                } else if ("Id".equalsIgnoreCase(selector)) {
                    hasNonEmptyCondition = true;
                    app.krista.extensions.crm.sales.salesforce_sales.service.Product product = productDao.getProductById(matchPattern);
                    if (product != null) {
                        products.add(ProductCatalogType.fromSFProduct(product));
                    }
                }
                // Unknown conditions (e.g. logicalName injected by framework) are intentionally ignored
            }
            if (!hasNonEmptyCondition) {
                LOGGER.info("All search conditions are empty, returning all products");
                return productDao.getProducts(Math.max(pageSize, 200), pageIndex).stream()
                        .map(ProductCatalogType::fromSFProduct)
                        .collect(Collectors.toList());
            }
            return products;
        } catch (IllegalArgumentException cause) {
            LOGGER.error(cause.getMessage(), cause);
            return List.of();
        }
    }

    @Override
    public long count(List<SearchCondition> list) {
        return 0;
    }

    @Override
    public List<String> lookup(Map<String, Object> map, List<String> list) {
        return null;
    }
}

