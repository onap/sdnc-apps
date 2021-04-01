package org.onap.sdnc.apps.ms.gra.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.onap.sdnc.apps.ms.gra.data.ConfigNetworks;
import org.onap.sdnc.apps.ms.gra.data.ConfigNetworksRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigServices;
import org.onap.sdnc.apps.ms.gra.data.ConfigServicesRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigVfModules;
import org.onap.sdnc.apps.ms.gra.data.ConfigVfModulesRepository;
import org.onap.sdnc.apps.ms.gra.data.ConfigVnfs;
import org.onap.sdnc.apps.ms.gra.data.ConfigVnfsRepository;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServiceData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworks;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworksNetwork;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataNetworksNetworkNetworkData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfs;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnf;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfData;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule;
import org.onap.sdnc.apps.ms.gra.swagger.model.GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfmoduleVfModuleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@ComponentScan(basePackages = { "org.onap.sdnc.apps.ms.gra.*", "org.onap.ccsdk.apps.services" })

public class ServiceDataHelper {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConfigServicesRepository configServicesRepository;
    
    @Autowired
    private ConfigNetworksRepository configNetworksRepository;

    @Autowired
    private ConfigVnfsRepository configVnfsRepository;

    @Autowired
    private ConfigVfModulesRepository configVfModulesRepository;

    public ServiceDataHelper() {

    }

    public class ServiceDataTransaction {
        private List<ConfigServices> servicesToSave = new LinkedList<ConfigServices>();
        private List<ConfigNetworks> networksToSave = new LinkedList<ConfigNetworks>();
        private List<ConfigVnfs> vnfsToSave = new LinkedList<ConfigVnfs>();
        private List<ConfigVfModules> vfModulesToSave = new LinkedList<ConfigVfModules>();

        private List<ConfigServices> servicesToRemove = new LinkedList<ConfigServices>();
        private List<ConfigNetworks> networksToRemove = new LinkedList<ConfigNetworks>();
        private List<ConfigVnfs> vnfsToRemove = new LinkedList<ConfigVnfs>();
        private List<ConfigVfModules> vfModulesToRemove = new LinkedList<ConfigVfModules>();

        // Deferred saves not working for now - do all immediate
        public void save(ConfigServices service) {
            // servicesToSave.add(service);
            configServicesRepository.save(service);
        }

        public void save(ConfigNetworks network) {
            // networksToSave.add(network);
            configNetworksRepository.save(network);
        }

        public void save(ConfigVnfs vnf) {
            // vnfsToSave.add(vnf);
            configVnfsRepository.save(vnf);
        }

        public void save(ConfigVfModules vfModule) {
            // vfModulesToSave.add(vfModule);
            configVfModulesRepository.save(vfModule);
        }

        public void remove(ConfigServices service) {
            // servicesToRemove.add(service);
            configServicesRepository.delete(service);
        }

        public void remove(ConfigNetworks network) {
            // networksToRemove.add(network);
            configNetworksRepository.delete(network);
        }

        public void remove(ConfigVnfs vnf) {
            // vnfsToRemove.add(vnf);
            configVnfsRepository.delete(vnf);
        }

        public void remove(ConfigVfModules vfModule) {
            // vfModulesToRemove.add(vfModule);
            configVfModulesRepository.delete(vfModule);
        }


        public void commit() {
 
            long numServicesBefore = configServicesRepository.count();
            long numNetworksBefore = configNetworksRepository.count();
            long numVnfsBefore = configVnfsRepository.count();
            long numVfModulesBefore = configVfModulesRepository.count();

            // Commit is a no-op for now
            return;
            /*
            for (ConfigServices service : servicesToSave) {
                configServicesRepository.save(service);
            }
            for (ConfigNetworks network : networksToSave) {
                configNetworksRepository.save(network);
            }
            for (ConfigVnfs vnf: vnfsToSave) {
                configVnfsRepository.save(vnf);
            }
            for (ConfigVfModules vfModule : vfModulesToSave) {
                configVfModulesRepository.save(vfModule);
            }
            for (ConfigServices service : servicesToRemove) {
                configServicesRepository.delete(service);
            }
            for (ConfigNetworks network : networksToSave) {
                configNetworksRepository.delete(network);
            }
            for (ConfigVnfs vnf: vnfsToSave) {
                configVnfsRepository.delete(vnf);
            }
            for (ConfigVfModules vfModule : vfModulesToSave) {
                configVfModulesRepository.delete(vfModule);
            }
            long numServicesAfter = configServicesRepository.count();
            long numNetworksAfter = configNetworksRepository.count();
            long numVnfsAfter = configVnfsRepository.count();
            long numVfModulesAfter = configVfModulesRepository.count();

            System.out.print("Done");
            */
        }
    }

    public ServiceDataTransaction createTransaction() {
        return new ServiceDataTransaction();
    }

    public GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule getVfModule(String svcInstanceId,
            String vnfId, String vfModuleId) throws JsonProcessingException {
        GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vfModule = null;
        List<ConfigVfModules> configVfModules = configVfModulesRepository
                .findBySvcInstanceIdAndVnfIdAndVfModuleId(svcInstanceId, vnfId, vfModuleId);
        if ((configVfModules != null) && !configVfModules.isEmpty()) {
            ConfigVfModules configVfModule = configVfModules.get(0);
             vfModule = new GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule();
            vfModule.setVfModuleId(configVfModule.getVfModuleId());
            String vfModuleDataAsString = configVfModule.getVfModuleData();
            if (vfModuleDataAsString != null) {
                vfModule.setVfModuleData(objectMapper.readValue(vfModuleDataAsString,
                        GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfmoduleVfModuleData.class));
            }

        }
        return (vfModule);
    }

    public GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules getVfModules(String svcInstanceId, String vnfId) throws JsonProcessingException {
        GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules vfModules = null;
        List<ConfigVfModules> configVfModules = configVfModulesRepository.findBySvcInstanceIdAndVnfId(svcInstanceId, vnfId);
        if ((configVfModules != null) && !configVfModules.isEmpty()) {
            vfModules = new GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules();
            for (ConfigVfModules configVfModule : configVfModules) {
                GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vfModule = new GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule();
                vfModule.setVfModuleId(configVfModule.getVfModuleId());
                String vfModuleDataAsString = configVfModule.getVfModuleData();
                if (vfModuleDataAsString != null) {
                    vfModule.setVfModuleData(objectMapper.readValue(vfModuleDataAsString, GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfmoduleVfModuleData.class));
                }
                vfModules.addVfModuleItem(vfModule);
            }
        }
        return(vfModules);
    }

    public GenericResourceApiServicedataServicedataVnfsVnf getVnf(String svcInstanceId, String vnfId) throws JsonProcessingException {
        GenericResourceApiServicedataServicedataVnfsVnf vnf = null;
        List<ConfigVnfs> configVnfs = configVnfsRepository.findBySvcInstanceIdAndVnfId(svcInstanceId, vnfId);
        if ((configVnfs != null) && !configVnfs.isEmpty()) {
            ConfigVnfs configVnf = configVnfs.get(0);
            vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
            vnf.setVnfId(configVnf.getVnfId());
            GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfDataAsObject = null;
            String vnfDataAsString = configVnf.getVnfData();
            if (vnfDataAsString != null) {
                vnfDataAsObject = objectMapper.readValue(vnfDataAsString, GenericResourceApiServicedataServicedataVnfsVnfVnfData.class);
            }

            GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules vfModules = getVfModules(svcInstanceId, vnfId);
            if (vfModules != null) {
                if (vnfDataAsObject == null) {
                    vnfDataAsObject = new GenericResourceApiServicedataServicedataVnfsVnfVnfData();
                }
                vnfDataAsObject.setVfModules(vfModules);
            }
            vnf.setVnfData(vnfDataAsObject);
        }
        return(vnf);
    }

    public GenericResourceApiServicedataServicedataVnfs getVnfs(String svcInstanceId) throws JsonProcessingException {

        GenericResourceApiServicedataServicedataVnfs vnfs = null;

        List<ConfigVnfs> configVnfs = configVnfsRepository.findBySvcInstanceId(svcInstanceId);
        if ((configVnfs != null) && !configVnfs.isEmpty()) {
            vnfs = new GenericResourceApiServicedataServicedataVnfs();
            for (ConfigVnfs configVnf : configVnfs) {
                GenericResourceApiServicedataServicedataVnfsVnf vnf = new GenericResourceApiServicedataServicedataVnfsVnf();
                vnf.setVnfId(configVnf.getVnfId());
                GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfDataAsObject = null;
                String vnfDataAsString = configVnf.getVnfData();
                if (vnfDataAsString != null) {
                    vnfDataAsObject = objectMapper.readValue(vnfDataAsString,
                            GenericResourceApiServicedataServicedataVnfsVnfVnfData.class);
                }

                GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules vfModules = getVfModules(svcInstanceId, configVnf.getVnfId());
                if (vfModules != null) {
                    if (vnfDataAsObject == null) {
                        vnfDataAsObject = new GenericResourceApiServicedataServicedataVnfsVnfVnfData();
                    }
                    vnfDataAsObject.setVfModules(vfModules);
                }
                vnf.setVnfData(vnfDataAsObject);
                vnfs.addVnfItem(vnf);
            }
        }
        return (vnfs);
    }

    public GenericResourceApiServicedataServicedataNetworks getNetworks(String svcInstanceId) throws JsonProcessingException {
        GenericResourceApiServicedataServicedataNetworks networks = null;
        List<ConfigNetworks> configNetworks = configNetworksRepository.findBySvcInstanceId(svcInstanceId);
        if ((configNetworks != null) && !configNetworks.isEmpty()) {
            networks = new GenericResourceApiServicedataServicedataNetworks();

            for (ConfigNetworks configNetwork : configNetworks) {
                GenericResourceApiServicedataServicedataNetworksNetwork network = new GenericResourceApiServicedataServicedataNetworksNetwork();
                network.setNetworkId(configNetwork.getNetworkId());
                String networkDataAsString = configNetwork.getNetworkData();
                if (networkDataAsString != null) {
                    network.setNetworkData(objectMapper.readValue(networkDataAsString, GenericResourceApiServicedataServicedataNetworksNetworkNetworkData.class));
                }
                networks.addNetworkItem(network);
            }
        }

        return(networks);
    }

    public String getServiceDataAsString(String svcInstanceId) throws JsonProcessingException {
        GenericResourceApiServicedataServiceData svcData = getServiceData(svcInstanceId);
        if (svcData == null) {
            return(null);
        } else {
            return(objectMapper.writeValueAsString(getServiceData(svcInstanceId)));
        }
    }

    public GenericResourceApiServicedataServiceData getServiceData(String svcInstanceId) throws JsonProcessingException {

        List<ConfigServices> configServices = configServicesRepository.findBySvcInstanceId(svcInstanceId);
        if ((configServices != null) && !configServices.isEmpty()) {
            return(getServiceData(configServices.get(0)));
        } else {
            return(null);
        }
    }

    public GenericResourceApiServicedataServiceData getServiceData(ConfigServices configService) throws JsonProcessingException {
        if (configService == null) {
            return(null);
        }

        GenericResourceApiServicedataServiceData svcData;
        String svcInstanceId = configService.getSvcInstanceId();
        String svcDataAsString = configService.getSvcData();

        if (svcDataAsString != null) {
            svcData = objectMapper.readValue(svcDataAsString, GenericResourceApiServicedataServiceData.class);
        } else {
            svcData = new GenericResourceApiServicedataServiceData();
        }

        // Add networks
        GenericResourceApiServicedataServicedataNetworks networks = getNetworks(svcInstanceId);
        if (networks != null) {
            svcData.setNetworks(networks);
        }

        //Add Vnfs
        GenericResourceApiServicedataServicedataVnfs vnfs = getVnfs(svcInstanceId);
        if (vnfs != null) {
            svcData.setVnfs(vnfs);
        }

        return(svcData);

    }

    public void saveVfModule(String svcInstanceId, String vnfId, GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vfModule, ServiceDataTransaction transaction) throws JsonProcessingException {
        if (vfModule == null) {
            return;
        }
        List<ConfigVfModules> configVfModules = configVfModulesRepository.findBySvcInstanceIdAndVnfIdAndVfModuleId(svcInstanceId, vnfId, vfModule.getVfModuleId());
        ConfigVfModules configVfModule;
        if ((configVfModules != null) && !configVfModules.isEmpty()) {
            configVfModule = configVfModules.get(0);
        } else {
            configVfModule = new ConfigVfModules(svcInstanceId, vnfId, vfModule.getVfModuleId());
        }
        if (vfModule.getVfModuleData() != null) {
            configVfModule.setVfModuleData(objectMapper.writeValueAsString(vfModule.getVfModuleData()));
        }
        if (transaction != null) {
            transaction.save(configVfModule);
        } else {
            configVfModulesRepository.save(configVfModule);
        }
    }

    public void saveVnf(String svcInstanceId, GenericResourceApiServicedataServicedataVnfsVnf vnf, ServiceDataTransaction transaction) throws JsonProcessingException {
        if (vnf == null) {
            return;
        }

        GenericResourceApiServicedataServicedataVnfsVnfVnfData vnfData = vnf.getVnfData();

        // Make a list of potential vf modules to remove
        HashMap<String, ConfigVfModules> vfModuleMap = new HashMap<>();
        List<ConfigVfModules> configVfModuleList = configVfModulesRepository.findBySvcInstanceIdAndVnfId(svcInstanceId, vnf.getVnfId());
        if ((configVfModuleList != null) && !configVfModuleList.isEmpty()) {
            for (ConfigVfModules configVfModuleItem : configVfModuleList) {
                vfModuleMap.put(configVfModuleItem.getVfModuleId(), configVfModuleItem);
            }
        }


        // Save vf modules
        GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfModules vfModules = vnfData.getVfModules();
        if (vfModules != null) {
            List<GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule> vfModuleList = vfModules.getVfModule();
            if ((vfModuleList != null) && !vfModuleList.isEmpty()) {
                for (GenericResourceApiServicedataServicedataVnfsVnfVnfdataVfmodulesVfModule vfModule : vfModuleList) {
                    saveVfModule(svcInstanceId, vnf.getVnfId(), vfModule, transaction);
                    if (vfModuleMap.containsKey(vfModule.getVfModuleId())) {
                        vfModuleMap.remove(vfModule.getVfModuleId());
                    }
                }
            }

            // Remove any vf Modules for this vnf id not in this list
            for (ConfigVfModules vfModuleToRemove : vfModuleMap.values()) {
                if (transaction != null) {
                    transaction.remove(vfModuleToRemove);
                } else {
                    configVfModulesRepository.delete(vfModuleToRemove);
                }
            }

            // Clear vf modules from vnfData before saving to avoid duplication
            vnfData.setVfModules(null);
        }

        // Save Vnf itself
        List<ConfigVnfs> configVnfs = configVnfsRepository.findBySvcInstanceIdAndVnfId(svcInstanceId, vnf.getVnfId());
        ConfigVnfs configVnf;
        if ((configVnfs != null) && !configVnfs.isEmpty()) {
            configVnf = configVnfs.get(0);
        } else {
            configVnf = new ConfigVnfs(svcInstanceId, vnf.getVnfId());
        }
        configVnf.setVnfData(objectMapper.writeValueAsString(vnfData));

        if (transaction != null) {
            transaction.save(configVnf);
        } else {
            configVnfsRepository.save(configVnf);
        }

    }

    public void saveNetwork(String svcInstanceId, GenericResourceApiServicedataServicedataNetworksNetwork network, ServiceDataTransaction transaction) throws JsonProcessingException {
        List<ConfigNetworks> configNetworks = configNetworksRepository.findBySvcInstanceIdAndNetworkId(svcInstanceId, network.getNetworkId());
        ConfigNetworks configNetwork;

        if ((configNetworks != null) && !configNetworks.isEmpty()) {
            configNetwork = configNetworks.get(0);
        } else {
            configNetwork = new ConfigNetworks(svcInstanceId, network.getNetworkId());
        }
        configNetwork.setNetworkData(objectMapper.writeValueAsString(network.getNetworkData()));
        if (transaction != null) {
            transaction.save(configNetwork);
        } else {
            configNetworksRepository.save(configNetwork);
        }
    }

    public void saveService(ConfigServices configService, String svcDataAsString) throws JsonMappingException, JsonProcessingException {
        saveService(configService, svcDataAsString, null);
    }


    public void saveService(ConfigServices configService, String svcDataAsString, ServiceDataTransaction transaction) throws JsonMappingException, JsonProcessingException {
        if (svcDataAsString == null) {
            configServicesRepository.save(configService);
        } else {
            saveService(configService, objectMapper.readValue(svcDataAsString, GenericResourceApiServicedataServiceData.class), transaction);
        }
    }

    public void saveService(ConfigServices configService, GenericResourceApiServicedataServiceData svcData) throws JsonProcessingException {
        saveService(configService, svcData, null);
    }

    public void saveService(ConfigServices configService, GenericResourceApiServicedataServiceData svcData, ServiceDataTransaction transaction) throws JsonProcessingException {
        
        if (svcData != null) {
            String svcInstanceId = configService.getSvcInstanceId();

            // Save networks
            GenericResourceApiServicedataServicedataNetworks networks = svcData.getNetworks();
            if (networks != null) {
                List<GenericResourceApiServicedataServicedataNetworksNetwork> networkList = networks.getNetwork();
                if ((networkList != null) && !networkList.isEmpty()) {
                    for (GenericResourceApiServicedataServicedataNetworksNetwork networkItem : networkList) {
                        saveNetwork(svcInstanceId, networkItem, transaction);
                    }
                }
            }
            svcData.setNetworks(null);

            // Save vnfs / vfModules
            GenericResourceApiServicedataServicedataVnfs vnfs = svcData.getVnfs();
            if (vnfs != null) {
                List<GenericResourceApiServicedataServicedataVnfsVnf> vnfList = vnfs.getVnf();
                if ((vnfList != null) && !vnfList.isEmpty()) {
                    for (GenericResourceApiServicedataServicedataVnfsVnf vnfItem : vnfList) {
                        saveVnf(svcInstanceId, vnfItem, transaction);
                    }
                }
            }
            svcData.setVnfs(null);

            configService.setSvcData(objectMapper.writeValueAsString(svcData));
        } else {

        }
        if (transaction != null) {
            transaction.save(configService);
        } else {
            configServicesRepository.save(configService);
        }
    }

    public long deleteVfModule(String svcInstanceId, String vnfId, String vfModuleId) {
        return(configVfModulesRepository.deleteBySvcInstanceIdAndVnfIdAndVfModuleId(svcInstanceId, vnfId, vfModuleId));
    }

    public long deleteVnf(String svcInstanceId, String vnfId) {
        configVfModulesRepository.deleteBySvcInstanceIdAndVnfId(svcInstanceId, vnfId);
        return(configVnfsRepository.deleteBySvcInstanceIdAndVnfId(svcInstanceId, vnfId));
    }

    public long deleteNetwork(String svcInstanceId, String networkId) {
        return(configNetworksRepository.deleteBySvcInstanceIdAndNetworkId(svcInstanceId, networkId));
    }

    public long deleteService(String svcInstanceId) {
        configVfModulesRepository.deleteBySvcInstanceId(svcInstanceId);
        configVnfsRepository.deleteBySvcInstanceId(svcInstanceId);
        configNetworksRepository.deleteBySvcInstanceId(svcInstanceId);
        return(configServicesRepository.deleteBySvcInstanceId(svcInstanceId));
    }

    public boolean hasServiceData(ConfigServices service) {
        if ((service.getSvcData() != null) && (service.getSvcData().length() > 0)) {
            return(true);
        }

        List<ConfigNetworks> networks = configNetworksRepository.findBySvcInstanceId(service.getSvcInstanceId());
        if ((networks != null) && !networks.isEmpty()) {
            return(true);
        }

        List<ConfigVnfs> vnfs = configVnfsRepository.findBySvcInstanceId(service.getSvcInstanceId());
        if ((vnfs != null) && !vnfs.isEmpty()) {
            return(true);
        }

        return(false);

    }
    public void clearServiceData(ConfigServices service) {
        configVfModulesRepository.deleteBySvcInstanceId(service.getSvcInstanceId());
        configVnfsRepository.deleteBySvcInstanceId(service.getSvcInstanceId());
        configNetworksRepository.deleteBySvcInstanceId(service.getSvcInstanceId());
        service.setSvcData(null);
        configServicesRepository.save(service);
    }

}
