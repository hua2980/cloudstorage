package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    private final UserService userService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService, UserService userService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    public List<Credential> selectByUsername(String username){
        User user = userService.getUser(username);
        return credentialMapper.selectByUserId(user.getUserId());
    }

    public Map<Integer, String> credentialMap(List<Credential> credentials){
        Map<Integer, String> credentialMap = new HashMap<>();
        // decrypt password
        for (Credential credential : credentials) {
            String decryptPassword = decodePassword(credential.getKey(), credential.getPassword());
            credentialMap.put(credential.getCredentialId(), decryptPassword);
        }
        return credentialMap;
    }

    public int addCredential(Credential credential, String username){
        // get userId
        User user = userService.getUser(username);

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        // modify credential object
        credential.setPassword(encryptPassword);
        credential.setKey(encodedKey);
        credential.setUserId(user.getUserId());

        System.out.println(credential);

        return credentialMapper.addCredential(credential);
    }

    public String decodePassword(String key, String password){
        return encryptionService.decryptValue(password, key);
    }

    public int updateCredential(Credential credential){
        // get old credential
        Credential oldCredential = credentialMapper.selectByCredentialId(credential.getCredentialId());
        // get the new encrypted password
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), oldCredential.getKey());
        credential.setPassword(encryptedPassword);
        System.out.println(credential);

        // update
        return credentialMapper.updateCredential(credential);
    }

    public int deleteCredentialById(Integer credentialId){
        return credentialMapper.deleteByCredentialId(credentialId);
    }

    public Credential selectByCredentialId(Integer credentialId){
        return credentialMapper.selectByCredentialId(credentialId);
    }


}
