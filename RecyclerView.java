/*
主要组件： recyclerView 、 ViewHolder 、 ViewAdapter 
*/
public class FileListFragment extends Fragment {
	private RecyclerView mRecyclerView; 		// 一个 recycler view 实例
	private FileAdapter mAdapter;          		//适配器，用来把数据传递给 recycler view 里面的 view holder 
	
	
	@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_file_list,container,false);
        mRecyclerView=view.findViewById(R.id.file_recycler_view);				//实例化 recycler view 布局资源
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//必须立即传递给layout manager

        updateUI();		//绑定 recycler view 的 Adapter ，更新 recyclerView
        return view;
    }
	
	private void updateUI(){
        List<File> files=  new LinkedList<>();
        /*((LinkedList<File>) files).addFirst(new File("Test for file adapter"));
        ((LinkedList<File>) files).add(new File("shit"));*/
        mAdapter=new FileAdapter(mFileManager.getFiles());
        //mAdapter=new FileAdapter(files);
        mRecyclerView.setAdapter(mAdapter);

    }
	
	 //view holder 用来托管一个 view ， recycler view  就是由一系列的 holder 构成的， holder 由 adapter 创建。
    private class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button mBtn;	//一个按钮
        private File mFile;		//当前视图 绑定一个文件数据 
        public FileHolder(LayoutInflater inflater,ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_file,parent,false));		//实例化资源
            mBtn=itemView.findViewById(R.id.btn_file_item);

        }
        public void bind(File file){				//把数据绑定到视图
            mFile=file;
            mBtn.setText(file.getAbsolutePath()+" : "+file.getName());
            mBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),
                    mFile.getAbsolutePath()+" is being clicked !", Toast.LENGTH_LONG).show();
            //文件类型判断，普通文件或者是文件夹！
            if(mFile.isFile()){
                Intent intent=FileDetailActivity.newIntent(getContext(),mFile);
                startActivity(intent);
            }else if(mFile.isDirectory()){
                Intent intent=new Intent(getContext(),FileListFragment.class);
                startActivity(intent);

            }

        }
    }
	
    //adapter 用来与 recycler view 进行通讯，构造 view holder 显示，同时对holder进行数据绑定
    private class FileAdapter extends RecyclerView.Adapter<FileHolder>{
        private List<File> mFiles;			//所有数据存储在 adapter 
        public FileAdapter(List<File> files){
            mFiles=files;
        }

        @NonNull
        @Override
        public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {	//这个回调函数构造 holder 
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());

            return new FileHolder(layoutInflater,parent);	//返回一个 view holder，显示到界面上 
        }

        @Override
        public void onBindViewHolder(@NonNull FileHolder holder, int position) {	//回调函数 ，进行数据绑定
            File file=mFiles.get(position);
            holder.bind(file);			//调用holder的 数据绑定方法

        }

        @Override
        public int getItemCount() {		//数据的个数
            return mFiles.size();
        }
    }
	
}


/* 一个 view holder资源： list_item_file.xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <Button
        android:id="@+id/btn_file_item"
        style="@style/Widget.AppCompat.Button.Colored"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Button" />
</LinearLayout>
*/