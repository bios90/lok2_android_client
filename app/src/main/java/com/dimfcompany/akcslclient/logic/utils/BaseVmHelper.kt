package com.dimfcompany.akcslclient.logic.utils

import com.dimfcompany.akcsl.logic.models.ModelDocument
import com.dimfcompany.akcslclient.R
import com.dimfcompany.akcslclient.base.extensions.emailIntent
import com.dimfcompany.akcslclient.base.extensions.getStringMy
import com.dimfcompany.akcslclient.base.extensions.openPdfIntent
import com.dimfcompany.akcslclient.base.extensions.openUrlIntent
import com.dimfcompany.akcslclient.base.mvvm.BaseViewModel
import com.dimfcompany.akcslclient.logic.BtnAction
import com.dimfcompany.akcslclient.logic.SharedPrefsManager
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderAlerter
import com.dimfcompany.akcslclient.logic.utils.builders.BuilderDownloader
import com.dimfcompany.akcslclient.logic.utils.files.FileManager
import com.dimfcompany.akcslclient.logic.utils.files.MyFileItem
import com.dimfcompany.barista.logic.builders.BuilderDialogBottom

class BaseVmHelper
{
    companion object
    {
        fun clickedDocumentLikeDislike(document: ModelDocument, base_vm: BaseViewModel)
        {
            val id = document.id ?: return
            val text: String
            if (SharedPrefsManager.hasInFavorites(id))
            {
                text = "Документ \"${document.title}\" удален из избранного"
                SharedPrefsManager.removeFromFavorite(id)
            }
            else
            {
                text = "Документ \"${document.title}\" добавлен в избранное"
                SharedPrefsManager.saveIdToFavorite(id)
            }

            BuilderAlerter.getGreenBuilder(text).sendInVm(base_vm)
        }

        fun clickedCard(document: ModelDocument, base_vm: BaseViewModel)
        {
            val btns: ArrayList<BtnAction> = arrayListOf()

            document.url_pdf?.let(
                {
                    val btn_pdf = BtnAction("Pdf", { checkAndOpenPdf(document,base_vm) }, getStringMy(R.string.faw_pdf))
                    btns.add(btn_pdf)
                })

            document.url_video?.let(
                {
                    val btn_video = BtnAction("Видео", { openUrlIntent(it) }, getStringMy(R.string.faw_video))
                    btns.add(btn_video)
                })

            document.url_html?.let(
                {
                    val btn_link = BtnAction("Ссылка", { openUrlIntent(it) }, getStringMy(R.string.faw_link))
                    btns.add(btn_link)
                })

            val btn_question = BtnAction("Задать вопрос",
                {
                    val user_name = SharedPrefsManager.pref_current_user.get().value?.getFullName()
                    val subj = "Вопрос из приложения хронос"
                    val text = "Пользователь: $user_name\nДокумент: ${document.title}"

                    emailIntent("ilyaevdokimovauditorapp@gmail.com", text, subj)
                },
                getStringMy(R.string.faw_question))

            btns.add(btn_question)

            BuilderDialogBottom()
                    .setBtns(btns)
                    .setTitle(document.title)
                    .sendInVm(base_vm)
        }

        private fun checkAndOpenPdf(item: ModelDocument, base_vm: BaseViewModel)
        {
            val name = item.pdf_file_name ?: return
            val url = item.url_pdf ?: return
            if (FileManager.checkIfFileExists(name))
            {
                val file = FileManager.getPdfFile(name)
                val my_file_item = MyFileItem.createFromFile(file)
                openPdfIntent(my_file_item.getUriForShare())
            }
            else
            {
                val file = FileManager.createPdfFile(name)
                BuilderDownloader()
                        .setUrl(url)
                        .setFileDestination(file)
                        .setBaseVm(base_vm)
                        .setActionSuccess(
                            {
                                openPdfIntent(it.getUriForShare())
                            })
                        .run()
            }
        }
    }
}