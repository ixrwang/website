/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.ixr.website.tools.utils;

import name.ixr.website.tools.utils.TranslateUtils.Language;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.text.MessageFormat;
import java.util.List;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * 地图工具包
 *
 * @author IXR
 */
public class MapsUtils {

    public static void main(String[] args) throws Exception {
        GeoInfo geoInfo = GetGeoInfo("北京故宫");
        System.out.println(geoInfo.toString());
    }
    /**
     * 获取地里信息
     * @param address 地址
     * @return 
     */
    public static GeoInfo GetGeoInfo(String address) {
        return GetGeoInfo(address, Language.CN);
    }
    /**
     * 获取地里信息
     * @param address 地址
     * @return 
     */
    public static GeoInfo GetGeoInfo(String address,Language hl) {
        try {
            String ip = InetAddress.getByName("maps.google.com").getHostAddress();
            String url = MessageFormat.format("http://{0}/maps/geo?q={1}&hl={2}", ip, address,hl.getLanguage());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HttpUtils.download(url, baos);
            String json = new String(baos.toByteArray(), "UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            GeoInfo geoInfo = objectMapper.readValue(json, GeoInfo.class);
            return geoInfo;
        } catch (Exception ex) {
            GeoInfo error = new GeoInfo();
            error.setName(address);
            GeoInfo.Status status = new GeoInfo.Status();
            status.setCode("500");
            status.setRequest(ex.getMessage());
            error.setStatus(status);
            return error;
        }
    }

    public static class GeoInfo {
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        @JsonProperty("Status")
        private Status Status;
        @JsonIgnore
        public Status getStatus() {
            return Status;
        }
        @JsonIgnore
        public void setStatus(Status Status) {
            this.Status = Status;
        }
        public static class Status {
            private String code;
            public String getCode() {
                return code;
            }
            public void setCode(String code) {
                this.code = code;
            }
            private String request;
            public String getRequest() {
                return request;
            }
            public void setRequest(String request) {
                this.request = request;
            }
        }
        @JsonProperty("Placemark")
        private List<Placemark> placemark;
        @JsonIgnore
        public List<Placemark> getPlacemark() {
            return placemark;
        }
        @JsonIgnore
        public void setPlacemark(List<Placemark> placemark) {
            this.placemark = placemark;
        }
        public static class Placemark {
            private String address;
            public String getAddress() {
                return address;
            }
            public void setAddress(String address) {
                this.address = address;
            }
            @JsonProperty("ExtendedData")
            private ExtendedData extendedData;
            @JsonIgnore
            public ExtendedData getExtendedData() {
                return extendedData;
            }
            @JsonIgnore
            public void setExtendedData(ExtendedData extendedData) {
                this.extendedData = extendedData;
            }
            public static class ExtendedData {
                @JsonProperty("LatLonBox")
                private LatLonBox latLonBox;
                @JsonIgnore
                public LatLonBox getLatLonBox() {
                    return latLonBox;
                }
                @JsonIgnore
                public void setLatLonBox(LatLonBox latLonBox) {
                    this.latLonBox = latLonBox;
                }
                public static class LatLonBox {
                    private String north;
                    public String getNorth() {
                        return north;
                    }
                    public void setNorth(String north) {
                        this.north = north;
                    }
                    private String south;
                    public String getSouth() {
                        return south;
                    }
                    public void setSouth(String south) {
                        this.south = south;
                    }
                    private String east;
                    public String getEast() {
                        return east;
                    }
                    public void setEast(String east) {
                        this.east = east;
                    }
                    private String west;
                    public String getWest() {
                        return west;
                    }
                    public void setWest(String west) {
                        this.west = west;
                    }
                }
            }
            @JsonProperty("Point")
            private Point point;
            @JsonIgnore
            public Point getPoint() {
                return point;
            }
            @JsonIgnore
            public void setPoint(Point point) {
                this.point = point;
            }
            public static class Point {
                private List<String> coordinates;
                public List<String> getCoordinates() {
                    return coordinates;
                }
                public void setCoordinates(List<String> coordinates) {
                    this.coordinates = coordinates;
                }
            }
        }

        @Override
        public String toString() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try {
                return objectMapper.writeValueAsString(this);
            } catch (IOException ex) {
                throw new Error(ex);
            }
        }
    }
}
