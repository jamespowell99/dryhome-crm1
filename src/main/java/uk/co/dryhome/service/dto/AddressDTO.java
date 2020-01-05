package uk.co.dryhome.service.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String address1;
    private String address2;
    private String address3;
    private String town;
    private String postCode;
}
