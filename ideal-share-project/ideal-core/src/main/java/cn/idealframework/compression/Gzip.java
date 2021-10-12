/*
 * Copyright 2021 cn.idealframework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.idealframework.compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP 压缩算法
 *
 * @author 宋志宗 on 2019/9/30
 */
@SuppressWarnings("SpellCheckingInspection")
public class Gzip {
  private static final int BUFFER_SIZE = 256;

  public static byte[] compress(byte[] bytes) {
    if (bytes.length == 0) {
      return bytes;
    }
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
         GZIPOutputStream gos = new GZIPOutputStream(baos)) {
      gos.write(bytes);
      gos.finish();
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] uncompress(byte[] bytes) {
    if (bytes.length == 0) {
      return bytes;
    }
    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
         GZIPInputStream gis = new GZIPInputStream(bais);
         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[BUFFER_SIZE];
      int n;
      while ((n = gis.read(buffer)) >= 0) {
        baos.write(buffer, 0, n);
      }
      baos.flush();
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] uncompress(byte[] bytes, int offset, int length) {
    if (bytes.length <= offset + length) {
      return new byte[0];
    }
    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes, offset, length);
         GZIPInputStream gis = new GZIPInputStream(bais);
         ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[BUFFER_SIZE];
      int n;
      while ((n = gis.read(buffer)) >= 0) {
        baos.write(buffer, 0, n);
      }
      baos.flush();
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
