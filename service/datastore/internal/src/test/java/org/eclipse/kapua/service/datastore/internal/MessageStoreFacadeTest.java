///*******************************************************************************
// * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
// *
// * This program and the accompanying materials are made
// * available under the terms of the Eclipse Public License 2.0
// * which is available at https://www.eclipse.org/legal/epl-2.0/
// *
// * SPDX-License-Identifier: EPL-2.0
// *
// * Contributors:
// *     Eurotech - initial API and implementation
// *******************************************************************************/
//package org.eclipse.kapua.service.datastore.internal;
//
//import org.eclipse.kapua.KapuaIllegalArgumentException;
//import org.eclipse.kapua.model.id.KapuaId;
//import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
//import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreMediator;
//import org.eclipse.kapua.service.datastore.internal.model.DatastoreMessageImpl;
//import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
//import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
//import org.eclipse.kapua.service.storable.model.id.StorableId;
//import org.eclipse.kapua.service.storable.model.id.StorableIdImpl;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * Message store facade
// *
// * @since 1.0.0
// */
//
//public class MessageStoreFacadeTest {
//
//    @Test
//    public void testBulkDelete() throws ClientException, ConfigurationException, KapuaIllegalArgumentException {
//        int counter = 0;
//        MessageStoreMediator mediator = Mockito.mock(MessageStoreMediator.class);
//        MessageStoreFacade messageStoreFacade = new MessageStoreFacade(Mockito.mock(ConfigurationProvider.class),mediator);
//        MessageStoreFacade spyMessageStoreFacade = Mockito.spy(messageStoreFacade);
//
//        List<DatastoreMessage> datastoreMessages = new ArrayList<>();
//        for (int i =0; i<10000; i++){
//            DatastoreMessage dm = new DatastoreMessageImpl();
//            dm.setTimestamp(new Date());
//            datastoreMessages.add(dm);
//        }
//        Mockito.doReturn(datastoreMessages.get(counter)).when(spyMessageStoreFacade).find(Mockito.any(),Mockito.any(),Mockito.any());
////        Mockito.doReturn(new DatastoreMessageImpl(){{setTimestamp(new Date());}}).when(spyMessageStoreFacade).find(Mockito.any(),Mockito.any(),Mockito.any());
////        Mockito.doReturn(new DatastoreMessageImpl(){{setTimestamp(new Date());}}).when(spyMessageStoreFacade).find(Mockito.any(),Mockito.any(),Mockito.any());
//
////        Mockito.doReturn(true).when(spyMessageStoreFacade).isDatastoreServiceEnabled(Mockito.any());
//
////        metadata = new Metadata("index","channel","client","metric");
////        Mockito.when(mediator.getMetadata(Mockito.any(KapuaId.class),Mockito.anyLong())).thenReturn(metadata);
//
//        KapuaId scopeId = new ScopeId("10");
//        StorableId id = new StorableIdImpl("c17db8ea-93ee-48b5-9b5d-8d278180d2d9");
//        for (int i =0; i<10000; i++){
//            counter++;
//            spyMessageStoreFacade.delete(scopeId, id);
//        }
//    }
//
//    @Test
//    public void testBulkDelete2() throws ClientException, ConfigurationException, KapuaIllegalArgumentException {
//        MessageStoreMediator mediator = Mockito.mock(MessageStoreMediator.class);
//        MessageStoreFacade messageStoreFacade = new MessageStoreFacade(Mockito.mock(ConfigurationProvider.class),mediator);
//        KapuaId scopeId = new ScopeId("10");
//        StorableId id = new StorableIdImpl("c17db8ea-93ee-48b5-9b5d-8d278180d2d9");
//        for (int i =0; i<100; i++){
//            messageStoreFacade.delete(scopeId, id);
//        }
//    }
//
//    @Test
//    public void testBulkDelete3() throws ClientException, ConfigurationException, KapuaIllegalArgumentException {
//        int counter = 0;
//        MessageStoreMediator mediator = Mockito.mock(MessageStoreMediator.class);
//        MessageStoreFacade messageStoreFacade = new MessageStoreFacade(Mockito.mock(ConfigurationProvider.class),mediator);
//        MessageStoreFacade spyMessageStoreFacade = Mockito.spy(messageStoreFacade);
//
//        DatastoreMessage dm = null;
//
//        Mockito.doReturn(dm).when(spyMessageStoreFacade).find(Mockito.any(),Mockito.any(),Mockito.any());
//
//        KapuaId scopeId = new ScopeId("10");
//        StorableId id = new StorableIdImpl("c17db8ea-93ee-48b5-9b5d-8d278180d2d9");
//        for (int i =0; i<100; i++){
//            dm = new DatastoreMessageImpl();
//            dm.setTimestamp(new Date());
//            spyMessageStoreFacade.delete(scopeId, id);
//            dm = null;
//        }
//    }
//
//    @Test
//    public void testBulkDelete4() throws ClientException, ConfigurationException, KapuaIllegalArgumentException {
//
//        MessageStoreMediator mediator = Mockito.mock(MessageStoreMediator.class);
//        MessageStoreFacade messageStoreFacade = new MessageStoreFacade(Mockito.mock(ConfigurationProvider.class),mediator);
//        MessageStoreFacade spyMessageStoreFacade = Mockito.spy(messageStoreFacade);
//
//        KapuaId scopeId = new ScopeId("10");
//        StorableId id = new StorableIdImpl("c17db8ea-93ee-48b5-9b5d-8d278180d2d9");
//        for (int i =0; i<10000; i++){
//            DatastoreMessage dm = new DatastoreMessageImpl();
//            dm.setTimestamp(new Date());
//            Mockito.doReturn(dm).when(spyMessageStoreFacade).find(Mockito.any(),Mockito.any(),Mockito.any());
//            spyMessageStoreFacade.delete(scopeId, id);
//            dm =null;
//        }
//    }
//
//    @Test
//    public void testBulkDelete5() throws ClientException, ConfigurationException, KapuaIllegalArgumentException {
//
//        MessageStoreMediator mediator = Mockito.mock(MessageStoreMediator.class);
//        MessageStoreFacade messageStoreFacade = new MessageStoreFacade(Mockito.mock(ConfigurationProvider.class),mediator);
//        MessageStoreFacade spyMessageStoreFacade = Mockito.spy(messageStoreFacade);
//
//        KapuaId scopeId = new ScopeId("10");
//        StorableId id = new StorableIdImpl("c17db8ea-93ee-48b5-9b5d-8d278180d2d9");
//        for (int i =0; i<10000; i++){
//            DatastoreMessage dm = new DatastoreMessageImpl();
//            dm.setTimestamp(new Date());
//            Mockito.doReturn(dm).when(spyMessageStoreFacade).find(Mockito.any(),Mockito.any(),Mockito.any());
//            spyMessageStoreFacade.delete(scopeId, id);
//            Mockito.doReturn(null).when(spyMessageStoreFacade).find(Mockito.any(),Mockito.any(),Mockito.any());
//            dm =null;
//        }
//    }
//}
