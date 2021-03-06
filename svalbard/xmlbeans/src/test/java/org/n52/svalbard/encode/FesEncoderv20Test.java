/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.Set;

import net.opengis.fes.x20.BBOXType;

import org.apache.xmlbeans.XmlObject;
import org.junit.Test;

import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.filter.FilterConstants.SpatialOperator;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.FesEncoderv20;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.collect.Maps;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
@Deprecated
public class FesEncoderv20Test {

    private final FesEncoderv20 fesEncoder = new FesEncoderv20();

    @Test
    public final void should_return_correct_encoder_keys() {
        final Set<EncoderKey> expectedKeySet =
                CodingHelper.encoderKeysForElements(FilterConstants.NS_FES_2, TemporalFilter.class,
                        org.n52.shetland.ogc.filter.FilterCapabilities.class, SpatialFilter.class);
        final Set<EncoderKey> returnedKeySet = fesEncoder.getKeys();

        assertThat(returnedKeySet.size(), is(3));
        assertThat(returnedKeySet, is(expectedKeySet));
    }

    @Test
    public final void should_return_emptyMap_for_supportedTypes() {
        assertThat(fesEncoder.getSupportedTypes(), is(not(nullValue())));
        assertThat(fesEncoder.getSupportedTypes().isEmpty(), is(TRUE));
    }

    @Test
    public final void should_return_emptySet_for_conformanceClasses() {
        assertThat(fesEncoder.getConformanceClasses(SosConstants.SOS, Sos2Constants.SERVICEVERSION), is(not(nullValue())));
        assertThat(fesEncoder.getConformanceClasses(SosConstants.SOS, Sos2Constants.SERVICEVERSION).isEmpty(), is(TRUE));
    }

    @Test
    public final void should_add_own_prefix_to_prefixMap() {
        final Map<String, String> prefixMap = Maps.newHashMap();
        fesEncoder.addNamespacePrefixToMap(prefixMap);
        assertThat(prefixMap.isEmpty(), is(FALSE));
        assertThat(prefixMap.containsKey(FilterConstants.NS_FES_2), is(TRUE));
        assertThat(prefixMap.containsValue(FilterConstants.NS_FES_2_PREFIX), is(TRUE));
    }

    @Test
    public final void should_not_fail_if_prefixMap_is_null() {
        fesEncoder.addNamespacePrefixToMap(null);
    }

    @Test
    public final void should_return_contentType_xml() {
        assertThat(fesEncoder.getContentType(), is(MediaTypes.TEXT_XML));
    }

    @Test
    public final void should_return_correct_schema_location() {
        assertThat(fesEncoder.getSchemaLocations().size(), is(1));
        final SchemaLocation schemLoc = fesEncoder.getSchemaLocations().iterator().next();
        assertThat(schemLoc.getNamespace(), is("http://www.opengis.net/fes/2.0"));
        assertThat(schemLoc.getSchemaFileUrl(), is("http://schemas.opengis.net/filter/2.0/filterAll.xsd"));
    }

    @Test(expected = UnsupportedEncoderInputException.class)
    public final void should_return_exception_if_received_null() throws OwsExceptionReport, EncodingException {
        fesEncoder.encode(null);
        fesEncoder.encode(null, null);
        fesEncoder.encode(null, EncodingContext.empty());
    }

    // @Test
    // deactivated until test fails on build server.
    public final void should_return_BBoxType_for_spatialFilter() throws EncodingException {
        final SpatialFilter filter = new SpatialFilter();
        filter.setOperator(SpatialOperator.BBOX);
        filter.setGeometry(new GeometryFactory().toGeometry(new Envelope(1, 2, 3, 4)));
        filter.setValueReference("valueReference");
        final XmlObject encode = fesEncoder.encode(filter);

        assertThat(encode, is(instanceOf(BBOXType.class)));
        final BBOXType xbBBox = (BBOXType) encode;
        assertThat(xbBBox.isSetExpression(), is(TRUE));
    }

}
