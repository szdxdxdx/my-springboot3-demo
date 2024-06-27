package team.example.util.expiringmap;

import team.project.util.expiringmap.ExpiringMap;

public class Demo {

    public static void main(String[] args) throws InterruptedException {

        /* 创建容器，设定键值对的有效时间是 5000 毫秒 */
        ExpiringMap<String, String> map = new ExpiringMap<>(5 * 1000);

        /* 添加键值对 */
        map.put("k1", "v1");
        map.put("k2", "v2");

        /* 等待 3 秒 */
        System.out.println("waiting 3 seconds...");
        Thread.sleep(3 * 1000);

        /* 获取键值对 */
        String v1 = map.get("k1");
        System.out.println("map[k1] = " + v1);
        String v2 = map.get("k2");
        System.out.println("map[k2] = " + v2);

        /* 查看剩余的有效时间（毫秒） */
        long remainingTime = map.getRemainingTime("k2");
        System.out.println("map[k2] remainingTime : " + remainingTime);

        /* 删除键值对 */
        map.remove("k1");
        System.out.println("map[k1] = " + map.get("k1"));

        /* 等待 3 秒，键值对过期 */
        System.out.println("waiting 3 seconds...");
        Thread.sleep(3 * 1000);
        System.out.println("map[k2] = " + map.get("k2"));
    }
}
