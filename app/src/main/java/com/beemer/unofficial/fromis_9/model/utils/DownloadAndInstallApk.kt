package com.beemer.unofficial.fromis_9.model.utils

import android.content.Intent
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.beemer.unofficial.fromis_9.view.view.ProgressDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class DownloadAndInstallApk(
    private val activity: AppCompatActivity,
    private val downloadUrl: String,
    private val fileName: String
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val progressDialog = ProgressDialog("다운로드 중...")

    fun startDownloadingApk() {
        scope.launch {
            withContext(Dispatchers.Main) {
                progressDialog.show(activity.supportFragmentManager, "progressDialog")
            }

            try {
                val file = downloadApk()
                withContext(Dispatchers.Main) {
                    progressDialog.dismiss()
                    if (file != null) {
                        installApk(file)
                    } else {
                        Toast.makeText(activity, "다운로드 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    progressDialog.dismiss()
                    Toast.makeText(activity, "다운로드 중 오류가 발생했습니다: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun downloadApk(): File? = withContext(Dispatchers.IO) {
        try {
            val url = URL(downloadUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val path = getDownloadPath()
            val outputFile = File("$path$fileName.apk")

            val directory = File(path)
            if (!directory.exists()) {
                directory.mkdirs()
            }

            downloadFile(connection, outputFile)
            return@withContext outputFile
        } catch (e: IOException) {
            return@withContext null
        }
    }

    private fun getDownloadPath(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/fromis_9/"
    }

    private suspend fun downloadFile(connection: HttpURLConnection, file: File) = withContext(Dispatchers.IO) {
        val fos = FileOutputStream(file)
        val inputStream = connection.inputStream

        val totalSize = connection.contentLength
        var downloadedSize = 0
        var lastUpdateSize = 0
        val updateThreshold = 1024 * 1024

        val buffer = ByteArray(1024)
        var len1: Int
        while (inputStream.read(buffer).also { len1 = it } != -1) {
            fos.write(buffer, 0, len1)
            downloadedSize += len1

            if (downloadedSize - lastUpdateSize >= updateThreshold) {
                val progress = (downloadedSize.toDouble() / totalSize.toDouble() * 100).toInt()
                withContext(Dispatchers.Main) {
                    progressDialog.setProgress(progress)
                }
                lastUpdateSize = downloadedSize
            }
        }
        fos.close()
        inputStream.close()
    }

    private fun installApk(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(activity, "${activity.packageName}.provider", file)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        activity.startActivity(intent)
    }
}