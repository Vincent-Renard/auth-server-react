package com.example.auth.server.authentification.facade.dtos.out;

import lombok.*;

/**
 * @autor Vincent
 * @date 24/03/2020
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@NoArgsConstructor
@Setter
@Getter
public class PubKey {

	byte[] key;

	public static PubKey from(byte[] c) {

		return new PubKey(c);
	}

}