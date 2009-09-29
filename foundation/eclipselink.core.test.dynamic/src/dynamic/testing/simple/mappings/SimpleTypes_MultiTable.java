/*******************************************************************************
 * Copyright (c) 1998, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     dclarke - Dynamic Persistence INCUBATION - Enhancement 200045
 *               http://wiki.eclipse.org/EclipseLink/Development/Dynamic
 *     mnorman - tweaks to work from Ant command-line,
 *               et database properties from System, etc.
 *     
 * This code is being developed under INCUBATION and is not currently included 
 * in the automated EclipseLink build. The API in this code may change, or 
 * may never be included in the product. Please provide feedback through mailing 
 * lists or the bug database.
 ******************************************************************************/
package dynamic.testing.simple.mappings;

//JUnit4 imports
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import junit.framework.Assert;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

//EclipseLink imports
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.dynamic.DynamicEntity;
import org.eclipse.persistence.dynamic.DynamicHelper;
import org.eclipse.persistence.dynamic.DynamicType;
import org.eclipse.persistence.dynamic.DynamicTypeBuilder;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.internal.dynamic.DynamicEntityImpl;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.queries.ReportQuery;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.UnitOfWork;

//domain-specific (testing) imports
import static dynamic.testing.DynamicTestingHelper.createSession;
import static dynamic.testing.DynamicTestingHelper.password;
import static dynamic.testing.DynamicTestingHelper.url;
import static dynamic.testing.DynamicTestingHelper.username;

public class SimpleTypes_MultiTable {

    //test fixtures
    static DatabaseSession session = null;
    static DynamicHelper dynamicHelper = null;
    
    @BeforeClass
    public static void setUp() {
        if (username == null) {
            fail("error retrieving database username");
        }
        if (password == null) {
            fail("error retrieving database password");
        }
        if (url == null) {
            fail("error retrieving database url");
        }
        session = createSession();
        dynamicHelper = new DynamicHelper(session);
        DynamicClassLoader dcl = dynamicHelper.getDynamicClassLoader();
        Class<?> simpleTypeA = dcl.createDynamicClass("model.SimpleA");
        DynamicTypeBuilder typeBuilder = new DynamicTypeBuilder(simpleTypeA, null, "SIMPLE_TYPE_A", "SIMPLE_TYPE_B", "SIMPLE_TYPE_C");
        typeBuilder.setPrimaryKeyFields("SIMPLE_TYPE_A.SID");
        typeBuilder.addDirectMapping("id", int.class, "SIMPLE_TYPE_A.SID");
        typeBuilder.addDirectMapping("value1", String.class, "SIMPLE_TYPE_A.VAL_1");
        typeBuilder.addDirectMapping("value2", boolean.class, "SIMPLE_TYPE_B.VAL_2");
        typeBuilder.addDirectMapping("value3", String.class, "SIMPLE_TYPE_B.VAL_3");
        typeBuilder.addDirectMapping("value4", double.class, "SIMPLE_TYPE_C.VAL_4");
        typeBuilder.addDirectMapping("value5", String.class, "SIMPLE_TYPE_C.VAL_5");
        dynamicHelper.addTypes(true, true, typeBuilder.getType());
    }

    @AfterClass
    public static void tearDown() {
        session.executeNonSelectingSQL("DROP TABLE SIMPLE_TYPE_C");
        session.executeNonSelectingSQL("DROP TABLE SIMPLE_TYPE_B");
        session.executeNonSelectingSQL("DROP TABLE SIMPLE_TYPE_A");
        session.logout();
        session = null;
        dynamicHelper = null;
    }

    @After
    public void clearDynamicTables() {
        session.executeNonSelectingSQL("DELETE FROM SIMPLE_TYPE_C");
        session.executeNonSelectingSQL("DELETE FROM SIMPLE_TYPE_B");
        session.executeNonSelectingSQL("DELETE FROM SIMPLE_TYPE_A");
    }
    
    @Test
    public void verifyConfig() throws Exception {
        ClassDescriptor descriptorA = dynamicHelper.getSession().getClassDescriptorForAlias("SimpleA");
        assertNotNull("No descriptor found for alias='SimpleA'", descriptorA);

        DynamicType simpleTypeA = dynamicHelper.getType("SimpleA");
        assertNotNull("'SimpleA' EntityType not found", simpleTypeA);
        assertEquals(descriptorA, simpleTypeA.getDescriptor());

        assertTrue(descriptorA.hasMultipleTables());
        assertEquals(3, descriptorA.getTables().size());

        DirectToFieldMapping a_id = (DirectToFieldMapping) descriptorA.getMappingForAttributeName("id");
        assertEquals(int.class, a_id.getAttributeClassification());
        DirectToFieldMapping a_value1 = (DirectToFieldMapping) descriptorA.getMappingForAttributeName("value1");
        assertEquals(String.class, a_value1.getAttributeClassification());

        DirectToFieldMapping a_value2 = (DirectToFieldMapping) descriptorA.getMappingForAttributeName("value2");
        assertEquals(boolean.class, a_value2.getAttributeClassification());

        DirectToFieldMapping a_value3 = (DirectToFieldMapping) descriptorA.getMappingForAttributeName("value3");
        assertEquals(String.class, a_value3.getAttributeClassification());

        DirectToFieldMapping a_value4 = (DirectToFieldMapping) descriptorA.getMappingForAttributeName("value4");
        assertEquals(double.class, a_value4.getAttributeClassification());

        DirectToFieldMapping a_value5 = (DirectToFieldMapping) descriptorA.getMappingForAttributeName("value5");
        assertEquals(String.class, a_value5.getAttributeClassification());
    }

    @Test
    public void verifyProperties() {
        DynamicType simpleTypeA = dynamicHelper.getType("SimpleA");
        Assert.assertNotNull(simpleTypeA);

        assertEquals(6, simpleTypeA.getNumberOfProperties());
        assertEquals("id", simpleTypeA.getPropertiesNames().get(0));
        assertEquals(int.class, simpleTypeA.getPropertyType(0));
        assertEquals("value1", simpleTypeA.getPropertiesNames().get(1));
        assertEquals(String.class, simpleTypeA.getPropertyType(1));
        assertEquals("value2", simpleTypeA.getPropertiesNames().get(2));
        assertEquals(boolean.class, simpleTypeA.getPropertyType(2));
        assertEquals("value3", simpleTypeA.getPropertiesNames().get(3));
        assertEquals(String.class, simpleTypeA.getPropertyType(3));
        assertEquals("value4", simpleTypeA.getPropertiesNames().get(4));
        assertEquals(double.class, simpleTypeA.getPropertyType(4));
        assertEquals("value5", simpleTypeA.getPropertiesNames().get(5));
        assertEquals(String.class, simpleTypeA.getPropertyType(5));

    }

    @Test
    public void createSimpleA() {
        DynamicType simpleTypeA = dynamicHelper.getType("SimpleA");
        Assert.assertNotNull(simpleTypeA);

        DynamicEntity a = simpleTypeA.newDynamicEntity();

        assertNotNull(a);
        assertTrue(a.isSet("id"));
        assertFalse(a.isSet("value1"));
        assertTrue(a.isSet("value2"));
        assertFalse(a.isSet("value3"));
        assertTrue(a.isSet("value4"));
        assertFalse(a.isSet("value5"));
    }

    @Test
    public void persistSimpleA() {
        DynamicType simpleTypeA = dynamicHelper.getType("SimpleA");
        Assert.assertNotNull(simpleTypeA);

        DynamicEntity simpleInstance = simpleTypeA.newDynamicEntity();
        simpleInstance.set("id", 1);
        simpleInstance.set("value1", "A1");

        UnitOfWork uow = session.acquireUnitOfWork();
        uow.registerNewObject(simpleInstance);
        uow.commit();

        ReportQuery countQuery = dynamicHelper.newReportQuery("SimpleA", new ExpressionBuilder());
        countQuery.addCount();
        countQuery.setShouldReturnSingleValue(true);
        int simpleCount = ((Number) session.executeQuery(countQuery)).intValue();
        Assert.assertEquals(1, simpleCount);

        session.release();
    }

    @Test
    public void verifyChangeTracking() {
        persistSimpleA();
        DynamicType simpleTypeA = dynamicHelper.getType("SimpleA");
        Assert.assertNotNull(simpleTypeA);

        UnitOfWork uow = session.acquireUnitOfWork();

        ReadObjectQuery findQuery = dynamicHelper.newReadObjectQuery("SimpleA");
        findQuery.setSelectionCriteria(findQuery.getExpressionBuilder().get("id").equal(1));
        DynamicEntityImpl a = (DynamicEntityImpl) uow.executeQuery(findQuery);

        assertNotNull(a);
        assertNotNull(a._persistence_getPropertyChangeListener());

        uow.release();
        session.release();
    }

}