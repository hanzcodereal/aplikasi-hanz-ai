# Hanz AI

Asisten obrolan pintar berbahasa Indonesia untuk Android, ditenagai Google Gemini.
Bisa menjawab pertanyaan dan menjelaskan isi gambar.

**Pengembang:** Hanz Dev Official

## Fitur
- Obrolan multi-giliran (Hanz AI mengingat percakapan sebelumnya)
- Kirim gambar untuk dianalisis
- Pilih model AI lewat menu titik tiga
- Tema gelap/terang otomatis, tampilan seluruhnya Bahasa Indonesia
- Logo & ikon aplikasi dibuat dengan XML (vector drawable, adaptive icon, ikon monokrom)

## Menjalankan di Android Studio
1. Ambil API Key di [Google AI Studio](https://aistudio.google.com/apikey).
2. Buat/isi file `local.properties` di folder utama proyek:
   ```properties
   apiKey=ISI_API_KEY_ANDA
   ```
3. Sync Gradle, lalu Run.

## Build APK Release lewat GitHub Actions
1. Unggah proyek ini ke repositori GitHub.
2. Buka **Settings → Secrets and variables → Actions → New repository secret**, lalu tambahkan:

   | Secret | Wajib? | Isi |
   |---|---|---|
   | `GEMINI_API_KEY` | Ya | API Key Gemini Anda |
   | `KEYSTORE_BASE64` | Disarankan | Keystore rilis dalam format base64 |
   | `KEYSTORE_PASSWORD` | Jika pakai keystore | Password keystore |
   | `KEY_ALIAS` | Jika pakai keystore | Alias kunci |
   | `KEY_PASSWORD` | Jika pakai keystore | Password kunci |

3. Buka tab **Actions → Build APK Release → Run workflow** (atau push ke `main`).
4. Setelah selesai, unduh APK di bagian **Artifacts**.
5. Untuk membuat halaman **Releases** otomatis, push tag: `git tag v1.0.0 && git push origin v1.0.0`.

### Membuat keystore sendiri (sekali saja)
```bash
keytool -genkeypair -v -keystore hanzai.jks -storetype PKCS12 -alias hanzai \
  -keyalg RSA -keysize 2048 -validity 10000
base64 -w 0 hanzai.jks   # salin hasilnya ke secret KEYSTORE_BASE64
```
Simpan file `.jks` dengan aman dan **jangan** di-commit. Tanpa keystore sendiri, workflow membuat kunci sementara
sehingga APK versi baru tidak bisa dipasang menimpa versi lama (harus uninstall dulu).

> **Catatan keamanan:** API Key ikut tertanam di dalam APK. Jangan membagikan APK secara luas dengan key pribadi;
> batasi key di Google AI Studio atau gunakan key khusus untuk aplikasi ini.

## Model AI
Daftar model ada di `ChatViewModel.kt` (`MODEL_OPTIONS`, `DEFAULT_MODEL`). Google rutin mematikan model lama,
jadi periksa [daftar model terbaru](https://ai.google.dev/gemini-api/docs/models) bila muncul pesan "model tidak tersedia".

---
Dasar proyek: Chat-Gemini-Native-Android oleh Sayed Ali Alkamel.
