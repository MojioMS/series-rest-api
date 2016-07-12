/*
 * Copyright (C) 2013-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public License
 * version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */
package org.n52.series.db.da.dao.v1;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.n52.series.db.da.DataAccessException;
import org.n52.series.db.da.beans.FeatureEntity;
import org.n52.series.db.da.beans.I18nFeatureEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class FeatureDao extends AbstractDao<FeatureEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureDao.class);

    public FeatureDao(Session session) {
        super(session);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FeatureEntity> find(DbQuery query) {
        LOGGER.debug("find instance: {}", query);
        Criteria criteria = getDefaultCriteria();
        if (hasTranslation(query, I18nFeatureEntity.class)) {
            criteria = query.addLocaleTo(criteria, I18nFeatureEntity.class);
        }
        criteria.add(Restrictions.ilike("name", "%" + query.getSearchTerm() + "%"));
        return criteria.list();
    }

    @Override
    public FeatureEntity getInstance(Long key, DbQuery parameters) throws DataAccessException {
        LOGGER.debug("get instance '{}': {}", key, parameters);
        return (FeatureEntity) session.get(FeatureEntity.class, key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<FeatureEntity> getAllInstances(DbQuery parameters) throws DataAccessException {
        Criteria criteria = createFeatureListCriteria(parameters, FeatureEntity.class);
        if (parameters.isPureStationInsituPlatformConcept()) {
            parameters.filterMobileInsitu("feature", criteria, false, true);
        }
        return (List<FeatureEntity>) criteria.list();
    }

    /**
     *
     * @param parameters
     * @return a list of features related to stationary insitu types
     * @since 2.0.0
     * @throws DataAccessException
     */
    @SuppressWarnings("unchecked")
    public List<FeatureEntity> getAllStations(DbQuery parameters) throws DataAccessException {
        Criteria criteria = createFeatureListCriteria(parameters, FeatureEntity.class);
        parameters.filterMobileInsitu("feature", criteria, false, true);
        return (List<FeatureEntity>) criteria.list();
    }

    /**
    *
    * @param parameters
    * @return a list of features related to mobile insitu types
    * @since 2.0.0
    * @throws DataAccessException
    */
   @SuppressWarnings("unchecked")
   public List<FeatureEntity> getAllMobileInsitu(DbQuery parameters) throws DataAccessException {
       Criteria criteria = createFeatureListCriteria(parameters, FeatureEntity.class);
       parameters.filterMobileInsitu("feature", criteria, true, true);
       return (List<FeatureEntity>) criteria.list();
   }

   private Criteria createFeatureListCriteria(DbQuery parameters, Class<? extends FeatureEntity> featureType) {
        Criteria criteria = getDefaultCriteria("feature", featureType);
        LOGGER.debug("get all instances: {}", parameters);
        if (hasTranslation(parameters, I18nFeatureEntity.class)) {
            parameters.addLocaleTo(criteria, I18nFeatureEntity.class);
        }
        DetachedCriteria filter = parameters.createDetachedFilterCriteria("feature");
        criteria.add(Subqueries.propertyIn("feature.pkid", filter));
        criteria = parameters.addPagingTo(criteria);
        parameters.addSpatialFilterTo(criteria, parameters);
        return criteria;
    }

    @Override
    protected Criteria getDefaultCriteria() {
        return getDefaultCriteria(null, FeatureEntity.class);
    }

}
