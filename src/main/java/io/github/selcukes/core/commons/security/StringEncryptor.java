/*
 * Copyright (c) Ramesh Babu Prudhvi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.selcukes.core.commons.security;

import io.github.selcukes.core.exception.EncryptionException;
import io.github.selcukes.core.helper.Preconditions;
import org.apache.commons.codec.binary.Base64;

import java.security.GeneralSecurityException;

public class StringEncryptor implements Encryptor {

    @Override
    public String encrypt(String text) {
        return encrypt(getCryptoKey(), text);
    }

    @Override
    public String decrypt(String text) {
        return decrypt(getCryptoKey(), text);
    }

    @Override
    public String encrypt(String cryptoKey, String text) {
        Preconditions.checkNotNull(text, "Password Text must not be null");
        Preconditions.checkNotNull(cryptoKey, "Crypto Key must not be null");
        try {
            byte[] textToEncrypt = ByteEncryptor.encryptData(cryptoKey, text.getBytes());
            return Base64.encodeBase64String(textToEncrypt);
        } catch (GeneralSecurityException e) {
            throw new EncryptionException(String.format("Failed Encrypting text[%s]: ", text), e);
        }
    }

    @Override
    public String decrypt(String cryptoKey, String encrypted) {
        Preconditions.checkNotNull(encrypted, "Encrypted Text must not be null");
        Preconditions.checkNotNull(cryptoKey, "Crypto Key must not be null");
        try {
            byte[] textToDecrypt = Base64.decodeBase64(encrypted);
            return new String(ByteEncryptor.decryptData(cryptoKey, textToDecrypt));
        } catch (Exception e) {
            throw new EncryptionException(String.format("Failed Decrypting text[%s] : ", encrypted), e);
        }
    }

    private String getCryptoKey() {
        return System.getProperty("selcukes.crypto.key");
    }


}
