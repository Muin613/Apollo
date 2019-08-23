package com.munin.music.data

class PictureData {

    var recommends: List<RecommendsBean>? = null

    class RecommendsBean {
        /**
         * recommendation_id : 6931
         * status : 2
         * type : boards
         * seq : 6
         * board_id : 13492345
         * user_id : 9718214
         * title : 书籍，海报
         * description :
         * category_id : design
         * pin_count : 796
         * follow_count : 630
         * like_count : 18
         * created_at : 1378867668
         * updated_at : 1565657945
         * deleting : 0
         * is_private : 0
         * user : {"username":"JICC","urlname":"jayjicc","pin_count":9775,"follow_count":2231,"user_id":9718214}
         * target_id : 13492345
         * url : https://huaban.com/boards/13492345/
         * cover : {"bucket":"hbimg","key":"5cd711b629ee3ee9e7a0c3e588037baa0171097f185c7-1cW2bW"}
         * username : 兔子爱家居
         * urlname : tuziaijia
         * avatar : {"id":27318866,"farm":"farm1","bucket":"hbimg","key":"a76836664705af4550cfa2539deab126101dab1e768c-Sbd8UW","type":"image/jpeg","width":400,"height":290,"frames":1}
         */

        var recommendation_id: Int = 0
        var status: Int = 0
        var type: String? = null
        var seq: Int = 0
        var board_id: Int = 0
        var user_id: Int = 0
        var title: String? = null
        var description: String? = null
        var category_id: String? = null
        var pin_count: Int = 0
        var follow_count: Int = 0
        var like_count: Int = 0
        var created_at: Int = 0
        var updated_at: Int = 0
        var deleting: Int = 0
        var is_private: Int = 0
        var user: UserBean? = null
        var target_id: Int = 0
        var url: String? = null
        var cover: CoverBean? = null
        var username: String? = null
        var urlname: String? = null
        var avatar: AvatarBean? = null

        class UserBean {
            /**
             * username : JICC
             * urlname : jayjicc
             * pin_count : 9775
             * follow_count : 2231
             * user_id : 9718214
             */

            var username: String? = null
            var urlname: String? = null
            var pin_count: Int = 0
            var follow_count: Int = 0
            var user_id: Int = 0
        }

        class CoverBean {
            /**
             * bucket : hbimg
             * key : 5cd711b629ee3ee9e7a0c3e588037baa0171097f185c7-1cW2bW
             */

            var bucket: String? = null
            var key: String? = null
        }

        class AvatarBean {
            /**
             * id : 27318866
             * farm : farm1
             * bucket : hbimg
             * key : a76836664705af4550cfa2539deab126101dab1e768c-Sbd8UW
             * type : image/jpeg
             * width : 400
             * height : 290
             * frames : 1
             */

            var id: Int = 0
            var farm: String? = null
            var bucket: String? = null
            var key: String? = null
            var type: String? = null
            var width: Int = 0
            var height: Int = 0
            var frames: Int = 0
        }
    }
}
