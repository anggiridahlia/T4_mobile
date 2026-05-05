# Tugas 4 - Student Directory App

**Nama:** Anggiri Dahlia Candra 
**NIM:** F1D02310038

## Deskripsi Singkat Aplikasi
Aplikasi **Student Directory** adalah aplikasi Android yang digunakan untuk mendata mahasiswa. Aplikasi ini mendukung operasi CRUD (Create, Read, Update, Delete) penuh, dilengkapi dengan fitur pencarian data secara *real-time* dan konfirmasi dialog untuk keamanan penghapusan data.

## Screenshots
*Catatan: Berikut adalah 4 halaman utama dari fitur CRUD aplikasi ini.*

![Halaman Utama](screenshots/home.png)
![Halaman Form](screenshots/form.png)
![Halaman Search](screenshots/search.png)
![Dialog Hapus](screenshots/delete.png)

## Metode Penyimpanan yang Digunakan
Aplikasi ini menggunakan **Room Database** (yang merupakan lapisan abstraksi dari SQLite).
**Alasannya:**
1. **Compile-time Verification:** Room melakukan pengecekan kueri SQL pada saat proses *compile*, sehingga mencegah *error* atau *typo* kueri saat aplikasi berjalan (*runtime*).
2. **Minim Boilerplate:** Dengan menggunakan anotasi DAO (Data Access Object), penulisan kode jauh lebih ringkas dibandingkan menggunakan SQLite murni (SQLiteOpenHelper).
3. **Integrasi Coroutines:** Room mendukung penggunaan Kotlin Coroutines (`Dispatchers.IO`), sehingga operasi *database* yang berat bisa dijalankan di *background thread* tanpa membuat tampilan aplikasi (UI) menjadi *freeze* atau *lag*.

## Kendala yang Dihadapi dan Cara Mengatasinya
Selama proses *development* dan *build*, terdapat beberapa kendala teknis yang signifikan:
1. **Error KSP "unexpected jvm signature V":** Terjadi bentrok (bug) pada *compiler* KSP versi terbaru saat memproses fungsi ber-keyword `suspend` yang tidak memiliki nilai kembalian (*Void*) di dalam DAO. 
   * **Cara Mengatasi:** Menyesuaikan nilai kembalian (return type) pada anotasi `@Insert` menjadi `Long` dan `@Update`/`@Delete` menjadi `Int`, atau opsi lainnya dengan mencabut *keyword* `suspend` jika operasi sudah dibungkus dengan *dispatcher background* yang tepat.
2. **Kendala Aar Dependency Compatibility:** Muncul pesan *error* bahwa library/dependensi membutuhkan versi API yang lebih tinggi.
   * **Cara Mengatasi:** Mengubah pengaturan versi SDK di dalam file `build.gradle.kts (Module: app)`. Nilai `compileSdk` dan `targetSdk` yang awalnya 34 dinaikkan menjadi 37 agar *library* modern dapat dikompilasi dengan lancar.
