package com.seepine.secret.quarkus.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.ExtensionSslNativeSupportBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class SecretQuarkusProcessor {
  private static final String FEATURE = "secret";

  @BuildStep
  FeatureBuildItem feature() {
    return new FeatureBuildItem(FEATURE);
  }

  @BuildStep
  public ExtensionSslNativeSupportBuildItem enableSsl() {
    return new ExtensionSslNativeSupportBuildItem(FEATURE);
  }
}
