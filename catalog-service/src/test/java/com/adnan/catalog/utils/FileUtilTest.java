package com.adnan.catalog.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

public class FileUtilTest {
    private static final String CATEGORY_IMAGE_DIR = "uploaded-images/categories";
    private static final String PRODUCT_IMAGE_DIR = "uploaded-images/products";
    private static final String CATEGORY_IMAGE_NAME = "photo-1592067284261-cb1092d519ba.jpg";
    private static final String PRODUCT_IMAGE_NAME = "81mOKbR4L.jpg";

    @Test
    public void testLoadFile() {
        assertThrows(RuntimeException.class, () -> FileUtil.loadFile("Download Dir", "foo.txt"));
    }

    @Test
    public void testLoadFile2() {
        String downloadDir = CATEGORY_IMAGE_DIR;
        String fileName = CATEGORY_IMAGE_NAME;
        Resource resource = FileUtil.loadFile(downloadDir, fileName);
        assertTrue(resource.isFile());
        assertEquals(fileName, resource.getFilename());
    }

    @Test
    public void testLoadFile3() {
        String downloadDir = PRODUCT_IMAGE_DIR;
        String fileName = PRODUCT_IMAGE_NAME;
        Resource resource = FileUtil.loadFile(downloadDir, fileName);
        assertTrue(resource.isFile());
        assertEquals(fileName, resource.getFilename());
    }
}

