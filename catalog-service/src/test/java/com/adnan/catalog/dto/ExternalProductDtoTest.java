package com.adnan.catalog.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class ExternalProductDtoTest {
    @Test
    public void testSetId() {
        Integer integer = 1;
        ExternalProductDto externalProductDto = new ExternalProductDto();
        externalProductDto.setId(integer);
        assertSame(integer, externalProductDto.getId());
    }

    @Test
    public void testSetTitle() {
        ExternalProductDto externalProductDto = new ExternalProductDto();
        externalProductDto.setTitle("Dr");
        assertEquals("Dr", externalProductDto.getTitle());
    }

    @Test
    public void testSetDescription() {
        ExternalProductDto externalProductDto = new ExternalProductDto();
        externalProductDto.setDescription("The characteristics of someone or something");
        assertEquals("The characteristics of someone or something", externalProductDto.getDescription());
    }

    @Test
    public void testSetImage() {
        ExternalProductDto externalProductDto = new ExternalProductDto();
        externalProductDto.setImage("Image");
        assertEquals("Image", externalProductDto.getImage());
    }

    @Test
    public void testSetPrice() {
        Integer integer = 1;
        ExternalProductDto externalProductDto = new ExternalProductDto();
        externalProductDto.setPrice(integer);
        assertSame(integer, externalProductDto.getPrice());
    }
}

