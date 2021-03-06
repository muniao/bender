/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright 2018 Nextdoor.com, Inc
 *
 */

package com.nextdoor.bender.operation.substitution.field;

import com.nextdoor.bender.config.AbstractConfig;
import com.nextdoor.bender.operation.substitution.Substitution;
import com.nextdoor.bender.operation.substitution.SubstitutionFactory;

public class FieldSubstitutionFactory implements SubstitutionFactory {
  public FieldSubstitutionConfig conf;

  @Override
  public void setConf(AbstractConfig config) {
    this.conf = (FieldSubstitutionConfig) config;
  }

  @Override
  public Class<FieldSubstitution> getChildClass() {
    return FieldSubstitution.class;
  }

  @Override
  public Substitution newInstance() {
    return new FieldSubstitution(this.conf.getKey(), this.conf.getSrcFields(),
        this.conf.getRemoveSrcField(), this.conf.getFailSrcNotFound(),
        this.conf.getFailDstNotFound());
  }
}
