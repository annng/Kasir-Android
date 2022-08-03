package com.artevak.kasirpos.common.const

const val TAG = "FamilyTree"
class DBConst {

    object DB{
        const val URL = "https://ecommerce-pos-4371b-default-rtdb.firebaseio.com/"
    }
    object TABLE {
        const val DEMO = "demo"
        const val FAMILY = "family"
        const val USER = "user"
        const val ITEM = "item"
    }

    object WEB_TYPE{
        const val PRIVACY_POLICY = "privacy_policy"
    }

    object WEB_URL{
        const val PRIVACY_POLICY = "https://www.freeprivacypolicy.com/live/137a22fc-0672-42e7-8904-fc399a4587ec"
    }

    companion object {
        const val LIMIT_IMAGE_SIZE = 512
        const val LIMIT_IMAGE_DIMEN = 1024
        fun getTable(): String {
            return TABLE.FAMILY
        }
    }

    object INTENT{
        const val REQ_DEFAULT = 0

        const val KEY_URL = "url"
        const val KEY_ITEM = "item"
        const val KEY_TYPE = "type"
    }

    object GENDER {
        const val KEY_MALE = "male"
        const val KEY_FEMALE = "female"
    }

    object ADS_ID_DEBUG{
        const val HOME_BANNER = "ca-app-pub-3940256099942544/6300978111"
        const val SUBMIT_INTERSETIAL = "ca-app-pub-3940256099942544/1033173712"
        const val SUBMIT_NATIVE = "ca-app-pub-3940256099942544/2247696110"
        const val AD_OPEN = "ca-app-pub-3940256099942544/3419835294"
    }

    object ADS_ID_RELEASE{
        const val HOME_BANNER = "ca-app-pub-3746017627108992/4201363680"
        const val SUBMIT_INTERSETIAL = "ca-app-pub-2149560571610642/6506805270"
        const val WHATSAPP_INTERSETIAL = "ca-app-pub-2149560571610642/5193723607"
        const val UNCONNECTED_INTERSETIAL = "ca-app-pub-2149560571610642/6803933141"
        const val UNCONNECTED_NATIVE = "ca-app-pub-2149560571610642/9689240495"
        const val AD_OPEN = "ca-app-pub-2149560571610642/2988506635"
    }
}