public class NavigatorFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);        //fragment manager �Ż�֪���в˵��ĳ�ʼ��
    }
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_all,menu);		// ʵ������������Դ
    }
	
	@Override//����������ť��Ӧ
    public boolean onOptionsItemSelected(MenuItem item) {
        final Intent pickContact= new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        switch (item.getItemId()){
            case R.id.mi_about:{
                AboutFragment aboutFragment=AboutFragment.newInstance("hunterX greet to people who love reverse engineering");			//����ʹ���� dialog Fragment 
                FragmentManager fragmentManager=getFragmentManager();       //ͨ�� fragment manager����ʾ�Ի���
                aboutFragment.show(fragmentManager,"show_about_msg");
                break;
            }
            case R.id.mi_config:{
                break;
            }
            case R.id.mi_share:{
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"��ʹ���������ţ�Ƶ�app����Ҳ���Կ���");
                intent=Intent.createChooser(intent,"��ı���");
                //startActivity(intent);
                startActivityForResult(pickContact,REQUEST_CONTACT);
                break;
            }
            default:
                break;
        }return super.onOptionsItemSelected(item);
    }
}

/*	menu.xml 
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    >
    <item
    android:id="@+id/mi_about"
    android:title="@string/about"
    app:showAsAction="ifRoom|withText"></item>
    <item android:id="@+id/mi_config"
        android:title="@string/config"
        app:showAsAction="withText|ifRoom"
        ></item>
    <item android:id="@+id/mi_share"
        android:title="@string/share"
        app:showAsAction="ifRoom|withText"></item>
</menu>
*/