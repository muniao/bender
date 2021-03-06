/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright 2017 Nextdoor.com, Inc
 *
 */

package com.nextdoor.bender.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import com.amazonaws.regions.Region;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.kms.model.DecryptRequest;
import com.amazonaws.services.kms.model.DecryptResult;
import com.amazonaws.util.Base64;

/**
 * Use AWS KMS to decrypt configuration passwords.
 */
public class Passwords {
  /**
   * Method to determine if function is running part of a unit test.
   *
   * From http://stackoverflow.com/a/12717377
   *
   * @return True if in unittest and False if standalone.
   */
  private static boolean isJUnitTest() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    List<StackTraceElement> list = Arrays.asList(stackTrace);
    for (StackTraceElement element : list) {
      if (element.getClassName().startsWith("org.junit.")) {
        return true;
      }
    }
    return false;
  }

  public static String decrypt(String str, Region region) throws UnsupportedEncodingException {
    if (isJUnitTest()) {
      return str;
    }

    AWSKMS kms = AWSKMSClientBuilder.standard().withRegion(region.getName()).build();

    /*
     * The KMS ciphertext is base64 encoded and must be decoded before the request is made
     */
    String cipherString = str;
    byte[] cipherBytes = Base64.decode(cipherString);

    /*
     * Create decode request and decode
     */
    ByteBuffer cipherBuffer = ByteBuffer.wrap(cipherBytes);
    DecryptRequest req = new DecryptRequest().withCiphertextBlob(cipherBuffer);
    DecryptResult resp = kms.decrypt(req);

    /*
     * Convert the response plaintext bytes to a string
     */
    return new String(resp.getPlaintext().array(), Charset.forName("UTF-8"));
  }
}
