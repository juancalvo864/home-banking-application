package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClientServiceImplementation implements ClientService {

    @Autowired
    private ClientRepository repoClient;


    @Override
    public List<Client> findAll() {
        return repoClient.findAll();
    }

    @Override
    public Client findById(Long id) {
        return repoClient.findById(id).orElse(null);
    }

    @Override
    public Client findByEmail(String email){
        return repoClient.findByEmail(email);
    }

    @Override
    public void save(Client client) {
        repoClient.save(client);
    }


}
