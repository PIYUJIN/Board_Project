package com.test.board_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.test.board_project.MainActivity.Companion.POST_READ_FRAGMENT
import com.test.board_project.databinding.FragmentPostListBinding
import com.test.board_project.databinding.RowPostListBinding

class PostListFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentPostListBinding: FragmentPostListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainActivity = activity as MainActivity
        fragmentPostListBinding = FragmentPostListBinding.inflate(inflater)

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
            }

            recyclerViewPostListAll.run{
                adapter = AllREcyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
                // divider 설정
                addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
            }

            recyclerViewPostListResult.run{
                adapter = ResultRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context)
                // divider 설정
                addItemDecoration(MaterialDividerItemDecoration(context, MaterialDividerItemDecoration.VERTICAL))
            }
        }

        return fragmentPostListBinding.root
    }

    // 모든 게시글 목록을 보여주는 리사이클러 뷰의 어뎁터
    inner class AllREcyclerViewAdapter : RecyclerView.Adapter<AllREcyclerViewAdapter.AllViewHolder>(){
        inner class AllViewHolder(rowPostListBinding: RowPostListBinding) : RecyclerView.ViewHolder(rowPostListBinding.root){

            val rowPostListSubject: TextView
            val rowPostListNickName: TextView

            init{
                rowPostListSubject = rowPostListBinding.rowPostListSubject
                rowPostListNickName = rowPostListBinding.rowPostListNickName

                rowPostListBinding.root.setOnClickListener {
                    mainActivity.replaceFragment(POST_READ_FRAGMENT, true, null)
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
            return 100
        }

        override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
            holder.rowPostListSubject.text = "제목입니다 : $position"
            holder.rowPostListNickName.text = "작성자 : $position"
        }
    }


    // 검색 결과 게시글 목록을 보여주는 리사이클러 뷰의 어뎁터
    inner class ResultRecyclerViewAdapter : RecyclerView.Adapter<ResultRecyclerViewAdapter.ResultViewHolder>(){
        inner class ResultViewHolder(rowPostListBinding: RowPostListBinding) : RecyclerView.ViewHolder(rowPostListBinding.root){

            val rowPostListSubject: TextView
            val rowPostListNickName: TextView

            init{
                rowPostListSubject = rowPostListBinding.rowPostListSubject
                rowPostListNickName = rowPostListBinding.rowPostListNickName
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
            val rowPostListBinding = RowPostListBinding.inflate(layoutInflater)
            val allViewHolder = ResultViewHolder(rowPostListBinding)

            rowPostListBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return allViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
            holder.rowPostListSubject.text = "제목입니다 : $position"
            holder.rowPostListNickName.text = "작성자 : $position"
        }
    }
}


