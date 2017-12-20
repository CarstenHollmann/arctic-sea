/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.shetland.ogc.ows;

import java.net.URI;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class OwsValuesUnit extends OwsDomainMetadata {
    public OwsValuesUnit(URI reference, String value) {
        super(reference, value);
    }

    public OwsValuesUnit(String value) {
        super(value);
    }

    public OwsValuesUnit(URI reference) {
        super(reference);
    }

    public boolean isUOM() {
        return false;
    }

    public boolean isReferenceSystem() {
        return false;
    }

    public OwsUOM asUOM() {
        throw new UnsupportedOperationException();
    }

    public OwsReferenceSystem asReferenceSystem() {
        throw new UnsupportedOperationException();
    }


}