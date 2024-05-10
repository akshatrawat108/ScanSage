package com.abhinav.idanalyzer.feature_id_analyzer.domain

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class IdAnalyzer : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()

    var date: RealmInstant = RealmInstant.now()

    var imageData: ByteArray = byteArrayOf()

    var isCard: Boolean = false

    var isPersonWithoutId: Boolean = false

    var owner_id: String = ""
}
