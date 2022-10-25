package com.carlosmarin.reactor.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithComments {

    private User user;
    private Comments comments;
}
