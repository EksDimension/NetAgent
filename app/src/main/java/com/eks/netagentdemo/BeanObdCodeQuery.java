package com.eks.netagentdemo;

/**
 * Created by Riggs on 2020/3/2
 */
public class BeanObdCodeQuery {

    /**
     * reason : success
     * result : {"code":"P2079","sycx":"该OBD故障码适用于所有汽车制造商","zwhy":"进气歧管调谐（IMT）阀门位置传感器/开关电路间歇 （第1排）","ywhy":"Intake Manifold Tuning (IMT) Valve Position Sensor/Switch Circuit Intermittent (Bank 1)","gzfw":"燃油, 空气或排放控制","ms":"如果进气歧管通路（runner）的长度最佳，气门关闭所产生的压力波会帮助将油气混合物导入发动机。问题是发动机不同转速对应的最佳进气歧管通路长度也不同。歧管调谐阀（MTV）的作用就是根据发动机转速，将进气导入不同长度的进气歧管通路，以此来优化发动机在不同转速时的扭矩。"}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * code : P2079
         * sycx : 该OBD故障码适用于所有汽车制造商
         * zwhy : 进气歧管调谐（IMT）阀门位置传感器/开关电路间歇 （第1排）
         * ywhy : Intake Manifold Tuning (IMT) Valve Position Sensor/Switch Circuit Intermittent (Bank 1)
         * gzfw : 燃油, 空气或排放控制
         * ms : 如果进气歧管通路（runner）的长度最佳，气门关闭所产生的压力波会帮助将油气混合物导入发动机。问题是发动机不同转速对应的最佳进气歧管通路长度也不同。歧管调谐阀（MTV）的作用就是根据发动机转速，将进气导入不同长度的进气歧管通路，以此来优化发动机在不同转速时的扭矩。
         */

        private String code;
        private String sycx;
        private String zwhy;
        private String ywhy;
        private String gzfw;
        private String ms;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSycx() {
            return sycx;
        }

        public void setSycx(String sycx) {
            this.sycx = sycx;
        }

        public String getZwhy() {
            return zwhy;
        }

        public void setZwhy(String zwhy) {
            this.zwhy = zwhy;
        }

        public String getYwhy() {
            return ywhy;
        }

        public void setYwhy(String ywhy) {
            this.ywhy = ywhy;
        }

        public String getGzfw() {
            return gzfw;
        }

        public void setGzfw(String gzfw) {
            this.gzfw = gzfw;
        }

        public String getMs() {
            return ms;
        }

        public void setMs(String ms) {
            this.ms = ms;
        }
    }

    @Override
    public String toString() {
        return "BeanObdCodeQuery{" +
                "reason='" + reason + '\'' +
                ", result=" + result +
                ", error_code=" + error_code +
                '}';
    }
}
