package com.carlosmarin.reactor.app.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Comments {
    @Singular
    private List<String> comments;

}
