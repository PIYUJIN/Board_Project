package com.test.board_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.test.board_project.MainActivity.Companion.POST_READ_FRAGMENT
import com.test.board_project.databinding.FragmentPostListBinding
import com.test.board_project.databinding.RowPostListBinding
import com.test.board_project.vm.PostViewModel

class PostListFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentPostListBinding: FragmentPostListBinding

    lateinit var postViewModel: PostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        fragmentPostListBinding = FragmentPostListBinding.inflate(inflater)

        postViewModel = ViewModelProvider(mainActivity)[PostViewModel::class.java]
        postViewModel.run{
            postDataList.observe(mainActivity){
                fragmentPostListBinding.recyclerViewPostListAll.adapter?.notifyDataSetChanged()
                fragmentPostListBinding.recyclerViewPostListResult.adapter?.notifyDataSetChanged()
            }
        }

        fragmentPostListBinding.run{

            searchBarPostList.run{
                hint = "검색어를 입력해주세요"
                inflateMenu(R.menu.menu_post_list)

                setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.item_post_list_add -> {
                            mainActivity.replaceFragment(MainActivity.POST_WRITE_FRAGMENT, true, null)
                        }
                    }
                    true
                }
            }

            searchViewPostList.run{
                hint = "검색어를 입력해주세요"

                addTransitionListener { searchView, previousState, newState ->
                    // 서치바를 눌러 서치뷰가 보일 때
                    if(newState == com.google.android.material.search.SearchView.TransitionState.SHOWING) {
                        postViewModel.resetPostList()
                    }
                    // 서치뷰의 백버튼을 눌러 서치뷰가 사라지고 서치바가 보일 때
                    else if(newState == com.google.android.material.search.SearchView.TransitionState.HIDING) {
                        postViewModel.getPostAll(arguments?.getLong("postType")!!)
                    }
                }

                editText.setOnEditorActionListener { v, actionId, event ->
                    postViewModel.getSearchPostList(arguments?.getLong("postType")!!, editText.text.toString())
                    true
                }
            }

            recyclerViewPostListAll.run{
                adapter = AllRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
                // divider 설정
                addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
            }

            recyclerViewPostListResult.run{
                adapter = AllRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
                // divider 설정
                addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
            }

            // 게시판 타입 번호를 전달하여 게시글 정보를 가져온다.
            postViewModel.getPostAll(arguments?.getLong("postType")!!)

        }

        return fragmentPostListBinding.root
    }

    // 게시글 목록을 보여주는 리사이클러 뷰의 어뎁터
    inner class AllRecyclerViewAdapter : RecyclerView.Adapter<AllRecyclerViewAdapter.AllViewHolder>() {
        inner class AllViewHolder(rowPostListBinding: RowPostListBinding) : RecyclerView.ViewHolder(rowPostListBinding.root) {

            val rowPostListSubject: TextView
            val rowPostListNickName: TextView

            init {
                rowPostListSubject = rowPostListBinding.rowPostListSubject
                rowPostListNickName = rowPostListBinding.rowPostListNickName

                rowPostListBinding.root.setOnClickListener {
                    // 항목 번째 글 번호를 가져온다.
                    val readPostIdx = postViewModel.postDataList.value?.get(adapterPosition)?.postIdx
                    val newBundle = Bundle()
                    newBundle.putLong("readPostIdx", readPostIdx!!)
                    mainActivity.replaceFragment(MainActivity.POST_READ_FRAGMENT, true, newBundle)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViewHolder {
            val rowPostListBinding = RowPostListBinding.inflate(layoutInflater)
            val allViewHolder = AllViewHolder(rowPostListBinding)

            rowPostListBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return allViewHolder
        }

        override fun getItemCount(): Int {
            return postViewModel.postDataList.value?.size!!
        }

        override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
            holder.rowPostListSubject.text = postViewModel.postDataList.value?.get(position)?.postSubject
//            holder.rowPostListNickName.text = postViewModel.postWriterNicknameList.value?.get(position)
        }
    }
}


