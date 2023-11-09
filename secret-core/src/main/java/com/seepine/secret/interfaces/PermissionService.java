package com.seepine.secret.interfaces;

import com.seepine.secret.entity.AuthUser;
import java.util.Set;
import javax.annotation.Nonnull;

public interface PermissionService {
  @Nonnull
  Set<String> query(@Nonnull AuthUser authUser);
}
