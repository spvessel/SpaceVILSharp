import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObserverPrototype {
    public static void main(String... args) throws Exception {
        Map<String, BaseItems> allItems = new HashMap<>();

        Windows win1 = new Windows("win1");
        allItems.put("win1", win1);
        Windows win2 = new Windows("win2");
        allItems.put("win2", win2);

        Items item11 = new Items("item11");
        allItems.put("item11", item11);
        Items item12 = new Items("item12");
        allItems.put("item12", item12);
        Items item13 = new Items("item13");
        allItems.put("item13", item13);

        Items item1 = new Items("item1");
        allItems.put("item1", item1);
        Items item2 = new Items("item2");
        allItems.put("item2", item2);
        Items item3 = new Items("item3");
        allItems.put("item3", item3);
        Items item4 = new Items("item4");
        allItems.put("item4", item4);
        Items item5 = new Items("item5");
        allItems.put("item5", item5);
        Items item6 = new Items("item6");
        allItems.put("item6", item6);
        Items item7 = new Items("item7");
        allItems.put("item7", item7);
        Items item8 = new Items("item8");
        allItems.put("item8", item8);
        Items item9 = new Items("item9");
        allItems.put("item9", item9);
        Items item10 = new Items("item10");
        allItems.put("item10", item10);

        win1.setFocused(true);
        win1.setXPosition(0);
        win1.setYPosition(0);

        win2.setWidthPolicy(BaseItems.SizePolicy.Fixed);
        win2.setHeightPolicy(BaseItems.SizePolicy.Fixed);
        win2.setXPosition(0);
        win2.setYPosition(0);

        item1.setXPosition(0);
        item1.setYPosition(0);
        item1.setWidthPolicy(BaseItems.SizePolicy.Fixed);
        item1.setHeightPolicy(BaseItems.SizePolicy.Fixed);

        item2.setXPosition(0);
        item2.setYPosition(0);
        item2.setWidthPolicy(BaseItems.SizePolicy.Expand);
        item2.setHeightPolicy(BaseItems.SizePolicy.Expand);

        item3.setXPosition(0);
        item3.setYPosition(0);
        item3.setWidthPolicy(BaseItems.SizePolicy.Fixed);
        item3.setHeightPolicy(BaseItems.SizePolicy.Fixed);

        item4.setXPosition(0);
        item4.setYPosition(0);
        item4.setWidthPolicy(BaseItems.SizePolicy.Expand);
        item4.setHeightPolicy(BaseItems.SizePolicy.Expand);

        item5.setXPosition(0);
        item5.setYPosition(0);
        item5.setWidthPolicy(BaseItems.SizePolicy.Fixed);
        item5.setHeightPolicy(BaseItems.SizePolicy.Fixed);

        item6.setXPosition(0);
        item6.setYPosition(0);
        item6.setWidthPolicy(BaseItems.SizePolicy.Expand);
        item6.setHeightPolicy(BaseItems.SizePolicy.Expand);

        item11.setWidthPolicy(BaseItems.SizePolicy.Expand);
        item11.setHeightPolicy(BaseItems.SizePolicy.Expand);
        item11.setXPosition(0);
        item11.setYPosition(0);

        item7.setXPosition(0);
        item7.setYPosition(0);
        item7.setWidthPolicy(BaseItems.SizePolicy.Fixed);
        item7.setHeightPolicy(BaseItems.SizePolicy.Fixed);

        item8.setXPosition(0);
        item8.setYPosition(0);
        item8.setWidthPolicy(BaseItems.SizePolicy.Expand);
        item8.setHeightPolicy(BaseItems.SizePolicy.Expand);

        item12.setWidthPolicy(BaseItems.SizePolicy.Expand);
        item12.setHeightPolicy(BaseItems.SizePolicy.Expand);
        item12.setXPosition(0);
        item12.setYPosition(0);

        item9.setXPosition(0);
        item9.setYPosition(0);
        item9.setWidthPolicy(BaseItems.SizePolicy.Fixed);
        item9.setHeightPolicy(BaseItems.SizePolicy.Fixed);

        item10.setXPosition(0);
        item10.setYPosition(0);
        item10.setWidthPolicy(BaseItems.SizePolicy.Expand);
        item10.setHeightPolicy(BaseItems.SizePolicy.Expand);

        item13.setWidthPolicy(BaseItems.SizePolicy.Fixed);
        item13.setHeightPolicy(BaseItems.SizePolicy.Fixed);
        item13.setXPosition(0);
        item13.setYPosition(0);

        win1.addChildren(item1);
        win1.addChildren(item2);
        win1.addChildren(item13);
        win1.addChildren(item11);

        win2.addChildren(item3);
        win2.addChildren(item4);

        item11.addChildren(item5);
        item11.addChildren(item6);
        item11.addChildren(item12);

        item12.addChildren(item8);

        item8.addChildren(item7);

        item13.addChildren(item9);
        item9.addChildren(item10);

        String str = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (!str.equals("end")) {
            str = reader.readLine();

            String[] arr = str.split("\\s");
            BaseItems curruntItem = allItems.get(arr[0]);

            switch (arr[1]) {
                case "setFocused" :
                    ((Windows)curruntItem).setFocused(true);
                    break;

                case "setUnfocused" :
                    ((Windows)curruntItem).setFocused(false);
                    break;

                case "setXPosition" :
                    int x = Integer.parseInt(arr[2]);
                    curruntItem.setXPosition(x);
                    break;

                case "setYPosition" :
                    int y = Integer.parseInt(arr[2]);
                    curruntItem.setYPosition(y);
                    break;

                case "setWidthPolicy" :
                    if (arr[2].equals("Fixed")) curruntItem.setWidthPolicy(BaseItems.SizePolicy.Fixed);
                    else curruntItem.setWidthPolicy(BaseItems.SizePolicy.Expand);
                    break;

                case "setHeightPolicy" :
                    if (arr[2].equals("Fixed")) curruntItem.setHeightPolicy(BaseItems.SizePolicy.Fixed);
                    else curruntItem.setHeightPolicy(BaseItems.SizePolicy.Expand);
                    break;

                case "changeParent" :
                    BaseItems newParent = allItems.get(arr[2]);
                    newParent.addChildren(curruntItem);
                    break;

                case "setWidth" :
                    int w = Integer.parseInt(arr[2]);
                    curruntItem.setWidth(w);
                    break;

                case "setHeight" :
                    int h = Integer.parseInt(arr[2]);
                    curruntItem.setHeight(h);
                    break;

                default:
                    System.out.println("Command is not found");
                    break;
            }

            System.out.println();
        }

    }
}
