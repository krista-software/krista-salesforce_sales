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

package app.krista.extensions.crm.sales.salesforce_sales.catalog;

import app.krista.extensions.crm.sales.salesforce_sales.impl.AssetDaoImpl;
import app.krista.extensions.crm.sales.salesforce_sales.impl.AssetImpl;
import app.krista.extensions.crm.sales.salesforce_sales.model.SFAsset;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AssetsManagementAreaTest {

    private AssetDaoImpl assetDao;
    private AssetsManagementArea area;

    @Before
    public void setUp() throws Exception {
        assetDao = mock(AssetDaoImpl.class);
        var ctor = AssetsManagementArea.class.getDeclaredConstructor(AssetDaoImpl.class);
        ctor.setAccessible(true);
        area = ctor.newInstance(assetDao);
    }

    @Test
    public void testDeleteAnAsset_HappyPath() {
        Boolean res = area.deleteAnAsset("A1");
        assertTrue(res);
        verify(assetDao, times(1)).deleteAssetById("A1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteAnAsset_InvalidId_Throws() {
        area.deleteAnAsset(" ");
    }

    @Test
    public void testUpdateAnAsset_HappyPath() {
        app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Asset ent = new app.krista.extensions.crm.sales.salesforce_sales.catalog.entities.Asset();
        ent.id = "A2"; ent.name = "X";
        when(assetDao.getAssetById("A2")).thenReturn(new AssetImpl(SFAsset.from(ent)));
        var res = area.updateAnAsset("A2", "C1", "ACC1", true, "AssetName");
        assertNotNull(res);
        verify(assetDao, times(1)).updateAsset(any(AssetImpl.class));
        verify(assetDao, times(1)).getAssetById("A2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAnAsset_MissingId_Throws() {
        area.updateAnAsset("", "C1", "ACC1", true, "AssetName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAnAsset_MissingName_Throws() {
        area.updateAnAsset("A2", "C1", "ACC1", true, " ");
    }
}

