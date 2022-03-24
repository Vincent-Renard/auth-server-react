package com.example.auth.server.authentification.facade.dtos.in;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @autor Vincent
 * @date 25/06/2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BanDomainRequest {
	String domain;
}
