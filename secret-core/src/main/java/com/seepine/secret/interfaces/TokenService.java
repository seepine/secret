package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;
import com.seepine.secret.exception.TokenSecretException;

import javax.annotation.Nonnull;

/**
 * @author seepine
 * @since 0.0.6
 */
public interface TokenService {
	/**
	 * 通过对象生成token，需要在此做token-user的缓存逻辑
	 *
	 * @param authUser 用户信息
	 * @return token
	 * @throws TokenSecretException e
	 */
	@Nonnull
	String generate(@Nonnull AuthUser authUser) throws TokenSecretException;

	/**
	 * 获取缓存
	 *
	 * @param token token
	 * @return 值
	 * @throws TokenSecretException e
	 */
	@Nonnull
	AuthUser analyze(@Nonnull String token) throws TokenSecretException;

	/**
	 * 清理逻辑，例如退出登录时会调用，清理缓存等
	 *
	 * @param authUser 用户信息
	 * @throws TokenSecretException e
	 */
	void clear(@Nonnull AuthUser authUser) throws TokenSecretException;
}
