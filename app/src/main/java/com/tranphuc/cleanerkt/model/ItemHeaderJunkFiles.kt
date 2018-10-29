package com.tranphuc.cleanerkt.model

class ItemHeaderJunkFiles {
    private var title = ""
    private var total = ""

    constructor()
    constructor(title: String, total: String) {
        this.title = title
        this.total = total
    }

    public var Title: String
        get() {
            return title
        }
        set(value) {
            title = value
        }


    public var Total: String
        get() {
            return total
        }
        set(value) {
            total = value
        }
}