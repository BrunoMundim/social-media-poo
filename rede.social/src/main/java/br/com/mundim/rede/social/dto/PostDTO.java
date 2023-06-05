package br.com.mundim.rede.social.dto;

import lombok.Data;

@Data
public class PostDTO {

    private Long userId;
    private String postTitle;
    private String postBody;

}
