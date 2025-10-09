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

