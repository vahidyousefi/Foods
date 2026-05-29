package ir.vy.food.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Create table in database
@Entity(tableName = "table_food")
data class Foods(

    // Create primary key in database
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    val txtSubject: String,
    val txtDis1: String,
    val txtTime: String,
    val txtPrice: String,

    // Rename column in database
//    @ColumnInfo(name = "url")
    val urlImage: String,

    val numOfNumber: Int,
    val rating: Float,
)

val foodsList = arrayListOf(
    Foods(
        txtSubject = "رویال برگر",
        txtDis1 = "همبر 90 درصد گوشت",
        txtTime = "30",
        txtPrice = "200000",
        urlImage = "https://zoodex-cdn.s3.ir-thr-at1.arvanstorage.ir/MasterItem/184756/Ihno8CnqI9z04DHqINsvvgLTKKldUpaLjvlViGGy.jpg",
        numOfNumber = 28,
        rating = 1.5f
    ),
    Foods(
        txtSubject = "هات داگ",
        txtDis1 = "هات داگ مرغ 70%،  ۳ورق پنیر گودا، سس مخصوص، گوجه، خیارشور، کاهو، نان باگت",
        txtTime = "35",
        txtPrice = "150000",
        urlImage = "https://zoodex-cdn.s3.ir-thr-at1.arvanstorage.ir/MasterItem/101716/qWzdut9IgudD5ufZ13enB6ryUTfKRx5Y5gvHTU4U.jpg",
        numOfNumber = 73,
        rating = 4.2f
    ),
    Foods(
        txtSubject = "پیتزا سیر و استیک",
        txtDis1 = "راسته گوساله . میکس پنیر . قارچ. زیتون . فلفل دلمه . سس سیر",
        txtTime = "35",
        txtPrice = "457000",
        urlImage = "https://zoodex-cdn.s3.ir-thr-at1.arvanstorage.ir/MasterItem/184754/mKVwbNH1LaZBE4mx3wXF4O1LCTfUELByUmDxYvWn.jpg",
        numOfNumber = 135,
        rating = 3f
    ),
    Foods(
        txtSubject = "سینی کباب پردیس",
        txtDis1 = "یک سیخ برگ گوسفندی + یک سیخ بناب + یک سیخ برگ جوجه + یک سیخ بختیاری + گوجه و قارچ",
        txtTime = "55",
        txtPrice = "1600000",
        urlImage = "https://zoodex-cdn.s3.ir-thr-at1.arvanstorage.ir/MasterItem/211122/vbt7IRZNYPgahh7xKe8bt4seaoF6yEROi4qTeTsf.jpg",
        numOfNumber = 85,
        rating = 4f
    ),
    Foods(
        txtSubject = "چلو ایرانی زعفرانی",
        txtDis1 = "یک پرس چلو ایرانی زعفرانی بدون ته چین به همراه کره",
        txtTime = "15",
        txtPrice = "100000",
        urlImage = "https://zoodex-cdn.s3.ir-thr-at1.arvanstorage.ir/MasterItem/49292/PdfBQy4JonfDolLTBnilwlC2IMj3PWEK06WAOtXw.jpg",
        numOfNumber = 95,
        rating = 4.5f
    ),
    Foods(
        txtSubject = "چلو کباب کوبیده دو سیخ",
        txtDis1 = "دوسیخ کوبیده 100 گرمی+400 گرم برنج ایرانی+گوجه و دورچین",
        txtTime = "35",
        txtPrice = "248800",
        urlImage = "https://zoodex-cdn.s3.ir-thr-at1.arvanstorage.ir/MasterItem/492979/rqAm0bNAsNcQk3wy8kYIAR3eEqgQGcT29s6vPZXa.jpg",
        numOfNumber = 20015,
        rating = 4f
    ),
    Foods(
        txtSubject = "رولت وانیلی",
        txtDis1 = "500 گرمی با تزیین پودر پسته ، نارگیل",
        txtTime = "15",
        txtPrice = "184000",
        urlImage = "https://zoodex-cdn.s3.ir-thr-at1.arvanstorage.ir/MasterItem/109255/TZRIerx54vSvZPnAKAfufZrFiOfxCug1lL189NyH.jpg",
        numOfNumber = 531225,
        rating = 4f
    )
)