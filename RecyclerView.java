/*
��Ҫ����� recyclerView �� ViewHolder �� ViewAdapter 
*/
public class FileListFragment extends Fragment {
	private RecyclerView mRecyclerView; 		// һ�� recycler view ʵ��
	private FileAdapter mAdapter;          		//�����������������ݴ��ݸ� recycler view ����� view holder 
	
	
	@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_file_list,container,false);
        mRecyclerView=view.findViewById(R.id.file_recycler_view);				//ʵ���� recycler view ������Դ
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//�����������ݸ�layout manager

        updateUI();		//�� recycler view �� Adapter ������ recyclerView
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
	
	 //view holder �����й�һ�� view �� recycler view  ������һϵ�е� holder ���ɵģ� holder �� adapter ������
    private class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button mBtn;	//һ����ť
        private File mFile;		//��ǰ��ͼ ��һ���ļ����� 
        public FileHolder(LayoutInflater inflater,ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_file,parent,false));		//ʵ������Դ
            mBtn=itemView.findViewById(R.id.btn_file_item);

        }
        public void bind(File file){				//�����ݰ󶨵���ͼ
            mFile=file;
            mBtn.setText(file.getAbsolutePath()+" : "+file.getName());
            mBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),
                    mFile.getAbsolutePath()+" is being clicked !", Toast.LENGTH_LONG).show();
            //�ļ������жϣ���ͨ�ļ��������ļ��У�
            if(mFile.isFile()){
                Intent intent=FileDetailActivity.newIntent(getContext(),mFile);
                startActivity(intent);
            }else if(mFile.isDirectory()){
                Intent intent=new Intent(getContext(),FileListFragment.class);
                startActivity(intent);

            }

        }
    }
	
    //adapter ������ recycler view ����ͨѶ������ view holder ��ʾ��ͬʱ��holder�������ݰ�
    private class FileAdapter extends RecyclerView.Adapter<FileHolder>{
        private List<File> mFiles;			//�������ݴ洢�� adapter 
        public FileAdapter(List<File> files){
            mFiles=files;
        }

        @NonNull
        @Override
        public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {	//����ص��������� holder 
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());

            return new FileHolder(layoutInflater,parent);	//����һ�� view holder����ʾ�������� 
        }

        @Override
        public void onBindViewHolder(@NonNull FileHolder holder, int position) {	//�ص����� ���������ݰ�
            File file=mFiles.get(position);
            holder.bind(file);			//����holder�� ���ݰ󶨷���

        }

        @Override
        public int getItemCount() {		//���ݵĸ���
            return mFiles.size();
        }
    }
	
}


/* һ�� view holder��Դ�� list_item_file.xml
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