/**
 * Copyright (C) 2013-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.series.db.da.beans.v2;

import java.util.Date;

import com.vividsolutions.jts.geom.Geometry;
import java.util.Objects;

public class TrackLocationEntity implements Comparable<TrackLocationEntity> {

	private Long pkid;

    private Date timestamp;

    private Geometry geom;
    
    private Long seriesPkid;

    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public boolean isSetTimestamp() {
    	return getTimestamp() != null;
    }

    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }
    
    public boolean isSetGeom() {
    	return getGeom() != null && !getGeom().isEmpty();
    }
    
    public Long getSeriesPkid() {
        return seriesPkid;
    }

    public void setSeriesPkid(Long seriesPkid) {
        this.seriesPkid = seriesPkid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" [");
        sb.append(" id: ").append(pkid);
        return sb.append(" ]").toString();
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj instanceof TrackLocationEntity) {
    		TrackLocationEntity tl = (TrackLocationEntity) obj;
    		if (isSetGeom() && isSetTimestamp() && tl.isSetGeom() && tl.isSetTimestamp()) {
                return getTimestamp().equals(tl.getTimestamp())
                        && getGeom().equals(tl.getGeom());
//	    		return Objects.equal(getTimestamp(), tl.getTimestamp()) 
//	    				&& Objects.equal(getGeom(), tl.getGeom());
    		}
    	}
    	return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.timestamp);
        hash = 41 * hash + Objects.hashCode(this.geom);
        return hash;
    }

	@Override
	public int compareTo(TrackLocationEntity o) {
		if (o instanceof TrackLocationEntity) {
			TrackLocationEntity tle = (TrackLocationEntity) o;
			if (isSetTimestamp() && tle.isSetTimestamp()) {
				if (getTimestamp().before(tle.getTimestamp())) {
					return -1;
				} else {
					return 1;
				}
			} else if (isSetTimestamp() && !tle.isSetTimestamp()) {
				return -1;
			} else if (!isSetTimestamp() && tle.isSetTimestamp()) {
				return 1;
			}
		}
		return 0;
	}
}