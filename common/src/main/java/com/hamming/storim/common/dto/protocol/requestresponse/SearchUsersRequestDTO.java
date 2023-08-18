package com.hamming.storim.common.dto.protocol.requestresponse;

import com.hamming.storim.common.dto.protocol.ProtocolDTO;

public class SearchUsersRequestDTO extends ProtocolDTO {

    private String searchString;

    public SearchUsersRequestDTO(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchString() {
        return searchString;
    }

    @Override
    public String toString() {
        return "SearchUsersRequestDTO{" +
                "searchString='" + searchString + '\'' +
                '}';
    }
}
