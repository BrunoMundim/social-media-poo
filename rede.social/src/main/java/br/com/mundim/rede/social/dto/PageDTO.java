package br.com.mundim.rede.social.dto;

import lombok.Data;

@Data
public class PageDTO {

    private String pagePic;
    private String pageName;
    private String pageDescription;
    private Long creatorId;

}
