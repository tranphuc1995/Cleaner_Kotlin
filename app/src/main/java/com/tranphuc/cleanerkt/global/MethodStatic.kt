package com.tranphuc.cleanerkt.global

class MethodStatic {
    companion object {
        public fun convertBytesToGb(bytes: Long): String {

            if ((bytes / 1000000000) > 0) {

                return ("" + Math.round((bytes.toDouble() / 1000000000) * 10) / 10.0 + " GB");

            } else if ((bytes / 1000000) > 0) {

                return ("" + Math.round((bytes.toDouble() / 1000000) * 10) / 10.0 + " MB");

            } else if ((bytes / 1000) > 0) {

                return ("" + Math.round((bytes.toDouble() / 1000) * 10) / 10.0 + " kB");

            } else {

                return ("" + bytes + " byte");

            }
        }
    }
}