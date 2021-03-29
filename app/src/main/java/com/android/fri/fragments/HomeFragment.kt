package com.android.fri.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.fri.R
import com.android.fri.adapter.PictureAdapter
import com.android.fri.helper.DatabaseHandler
import com.android.fri.model.Picture
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray


class HomeFragment : Fragment() {

    private val pictures = ArrayList<Picture>()
    private lateinit var pictureAdapter: PictureAdapter
    private var page: Int = 1
    private var load = true
    lateinit var databaseHandler: DatabaseHandler
    private var search: String = "arivera2015"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        val activity = activity as Context
        val layoutManager = LinearLayoutManager(activity)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        pictureAdapter = PictureAdapter(activity, pictures)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = pictureAdapter
        databaseHandler = DatabaseHandler(requireActivity().applicationContext)

        (activity as AppCompatActivity).supportActionBar?.title = "Home"


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    if(load){
                        if ((visibleItemCount + pastVisibleItem) >= totalItemCount) {
                            load = false
                            page++
                            consumeRest()
                        }
                    }
                }

            }
        })

        pictureAdapter.onItemClick = { picture ->

            (activity as AppCompatActivity).supportActionBar?.title = "Detail Home"

            requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_wrapper, DetailFragment.newInstance(picture.getLikes(), picture.getUserImage(),
                            picture.getName(), picture.getUserName(), picture.getPicture(), picture.getIsFavorite(), picture.getDescription(), picture.getUpdated_at(), picture.getBio()))
                    .commit()
        }

        load = true
        page = 1
        consumeRest()

        return view
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
                load = true
                page = 1
                consumeRest()
                if (query != null) {
                    search = query.trim()
                };
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun consumeRest(){
        val queue = Volley.newRequestQueue(activity)

        val url = "https://api.unsplash.com/users/"+ search +"/photos?client_id=1Ejy3P2RX8mbLwizY7y6hVBKzKHg39ZJhced4pzdbD8&page=" + page + "&per_page=10" + "&order_by=oldest"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response -> try{
                load = true
                val jsonArray = JSONArray(response)
                for(i in 0 until jsonArray.length()) {
                    val img = jsonArray.getJSONObject(i)
                    var description = ""
                    if(img.has("description") && !img.isNull("description")){
                        description = img.get("description").toString()
                    }
                    val like = img.get("likes").toString()
                    val urls = img.getJSONObject("urls")
                    val picture = urls.get("full").toString()
                    val user = img.getJSONObject("user")
                    val name = user.get("name").toString()
                    val userName = user.get("username").toString()
                    val profileImages = user.getJSONObject("profile_image")
                    val userImage = profileImages.get("medium").toString()
                    val updated_at = img.get("updated_at").toString()
                    var bio = "";
                    val bytes = ByteArray(0)
                    if(user.has("bio") && !user.isNull("bio")){
                        bio = user.get("bio").toString()
                    }

                    val imagen = Picture(0,like, userImage, name, userName, picture,
                            databaseHandler.existPicture(userName, updated_at) > 0,description,updated_at,bio,bytes,bytes)

                    pictures.add(imagen)
                    pictureAdapter.updatePictureList(pictures)
                }
            } catch (e: Exception){
                load = true
                println(e.toString())
            }},
            Response.ErrorListener { error ->  println("${error}")}
        )
        try {
            queue.add(stringRequest)
        } catch (e: Exception) {
            print(e.toString())
        }
    }


}