package com.android.fri.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.fri.R
import com.android.fri.adapter.PictureAdapter
import com.android.fri.helper.DatabaseHandler
import com.android.fri.model.Picture

class FavoriteFragment : Fragment() {

    private var pictures = ArrayList<Picture>()
    private lateinit var pictureAdapter: PictureAdapter
    lateinit var databaseHandler: DatabaseHandler;
    private var search: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_favorite, container, false)
        databaseHandler = DatabaseHandler(requireActivity().applicationContext)
        val activity = activity as Context
        val layoutManager = LinearLayoutManager(activity)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        pictureAdapter = PictureAdapter(activity, pictures)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = pictureAdapter

        (activity as AppCompatActivity).supportActionBar?.title = "Favorite"

        pictureAdapter.onItemClick = { picture ->
            (activity as AppCompatActivity).supportActionBar?.title = "Detail Favorite"
            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_wrapper, DetailFavoriteFragment.newInstance(picture.getLikes(), picture.getUserImageBlob(),
                            picture.getName(), picture.getUserName(), picture.getPictureBlob(), picture.getIsFavorite(), picture.getDescription(), picture.getUpdated_at(), picture.getBio()))
                    .commit()

        }

        pictureAdapter.onItemFavoriteClick = { picture ->
            try {
                databaseHandler.deletePicture(picture)
                loadPictures()
            }catch (e: Exception){
                print(e)
            }
        }

        loadPictures()

        if(pictures.isEmpty()){

            val builder = AlertDialog.Builder(activity)
            builder.setTitle("Favorites")
            builder.setMessage("Is Empty :) ")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                dialog.dismiss()
            }
            builder.show()

        }

        return view
    }


    private fun loadPictures(){
        try {
            pictures = databaseHandler.viewPictures(search) as ArrayList<Picture>
            pictureAdapter.updatePictureList(pictures)

        } catch (e: Exception) {
            print(e.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu);
        val item = menu.findItem(R.id.ic_home_search)
        val searchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(pictures.size > 0){
                    pictures.clear()
                }

                if (query != null) {
                    search = query
                    pictures = databaseHandler.viewPictures(search) as ArrayList<Picture>
                    pictureAdapter.updatePictureList(pictures)

                };
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {

                if(newText.isEmpty()){
                    search = newText
                    pictures = databaseHandler.viewPictures(search) as ArrayList<Picture>
                    pictureAdapter.updatePictureList(pictures)
                }

                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }


}