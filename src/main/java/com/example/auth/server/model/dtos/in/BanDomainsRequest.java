package com.example.auth.server.model.dtos.in;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

/**
 * @autor Vincent
 * @date 23/07/2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BanDomainsRequest {
    Collection<String> domains;
}
