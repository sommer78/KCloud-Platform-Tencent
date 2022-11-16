/**
 * Copyright (c) 2022 KCloud-Platform-Official Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.core.password;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.laokou.common.core.exception.CustomException;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * Implementation of PasswordEncoder that uses the BCrypt strong hashing function. Clients
 * can optionally supply a "strength" (a.k.a. log rounds in BCrypt) and a SecureRandom
 * instance. The larger the strength parameter the more work will have to be done
 * (exponentially) to hash the passwords. The default value is 10.
 *
 * @author Dave Syer
 *
 */
public class BcryptPasswordEncoder implements PasswordEncoder {
	private final Pattern bcryptPattern = Pattern
			.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
	private final Log logger = LogFactory.getLog(getClass());

	private final int strength;

	private final SecureRandom random;

	public BcryptPasswordEncoder() {
		this(-1);
	}

	/**
	 * @param strength the log rounds to use, between 4 and 31
	 */
	public BcryptPasswordEncoder(int strength) {
		this(strength, null);
	}

	/**
	 * @param strength the log rounds to use, between 4 and 31
	 * @param random the secure random instance to use
	 *
	 */
	public BcryptPasswordEncoder(int strength, SecureRandom random) {
		boolean flag = strength != -1 && (strength < Bcrypt.MIN_LOG_ROUNDS || strength > Bcrypt.MAX_LOG_ROUNDS);
		if (flag) {
			throw new CustomException("Bad strength");
		}
		this.strength = strength;
		this.random = random;
	}

	@Override
	public String encode(CharSequence rawPassword) {
		String salt;
		if (strength > 0) {
			if (random != null) {
				salt = Bcrypt.gensalt(strength, random);
			}
			else {
				salt = Bcrypt.gensalt(strength);
			}
		}
		else {
			salt = Bcrypt.gensalt();
		}
		return Bcrypt.hashpw(rawPassword.toString(), salt);
	}

    @Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || encodedPassword.length() == 0) {
			logger.warn("Empty encoded password");
			return false;
		}

		if (!bcryptPattern.matcher(encodedPassword).matches()) {
			logger.warn("Encoded password does not look like BCrypt");
			return false;
		}

		return Bcrypt.checkpw(rawPassword.toString(), encodedPassword);
	}
}
