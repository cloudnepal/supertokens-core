/*
 *    Copyright (c) 2023, VRAI Labs and/or its affiliates. All rights reserved.
 *
 *    This software is licensed under the Apache License, Version 2.0 (the
 *    "License") as published by the Apache Software Foundation.
 *
 *    You may not use this file except in compliance with the License. You may
 *    obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *    License for the specific language governing permissions and limitations
 *    under the License.
 */

package io.supertokens.webserver.api.multitenancy;

import com.google.gson.JsonObject;
import io.supertokens.Main;
import io.supertokens.pluginInterface.multitenancy.TenantIdentifier;
import io.supertokens.pluginInterface.multitenancy.exceptions.TenantOrAppNotFoundException;
import io.supertokens.webserver.InputParser;
import io.supertokens.webserver.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CreateOrUpdateAppV2API extends BaseCreateOrUpdate {

    private static final long serialVersionUID = -4641988458637882374L;

    public CreateOrUpdateAppV2API(Main main) {
        super(main);
    }

    @Override
    public String getPath() {
        return "/recipe/multitenancy/app/v2";
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        JsonObject input = InputParser.parseJsonObjectOrThrowError(req);

        String appId = InputParser.parseStringOrThrowError(input, "appId", true);
        if (appId != null) {
            appId = Utils.normalizeAndValidateAppId(appId);
        }

        TenantIdentifier sourceTenantIdentifier;

        try {
            sourceTenantIdentifier = getTenantIdentifier(req);
        } catch (TenantOrAppNotFoundException e) {
            throw new ServletException(e);
        }

        super.handle_v2(
                req, sourceTenantIdentifier,
                new TenantIdentifier(sourceTenantIdentifier.getConnectionUriDomain(), appId, null),
                input, resp);

    }
}