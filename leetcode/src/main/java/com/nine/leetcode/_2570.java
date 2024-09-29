package com.nine.leetcode;


import java.util.*;

/**
 * @author fan
 */
public class _2570 {

    public static void main(String[] args) {
//        int[][] nums1 = {{148, 597}, {165, 623}, {306, 359}, {349, 566}, {403, 646}, {420, 381}, {566, 543}, {730, 209}, {757, 875}, {788, 208}, {932, 695}};
//        int[][] nums2 = {{74, 669}, {87, 399}, {89, 165}, {99, 749}, {122, 401}, {138, 16}, {144, 714}, {148, 206}, {177, 948}, {211, 653}, {285, 775}, {309, 289}, {349, 396}, {386, 831}, {403, 318}, {405, 119}, {420, 153}, {468, 433}, {504, 101}, {566, 128}, {603, 688}, {618, 628}, {622, 586}, {641, 46}, {653, 922}, {672, 772}, {691, 823}, {693, 900}, {756, 878}, {757, 952}, {770, 795}, {806, 118}, {813, 88}, {919, 501}, {935, 253}, {982, 385}};

        int[][] nums1 = {{1, 2}, {2, 3}, {4, 5}};
        int[][] nums2 = {{1, 4}, {3, 2}, {4, 1}};

        // 第一种 map 解法
//        int[][] ints = _2570(nums1, nums2);

        // 第二种解法
        int[][] ints = mergeArrays(nums1, nums2);

        for (int[] as : ints) {
            System.out.println(Arrays.toString(as));
        }
    }

    /**
     * 给你两个 二维 整数数组 nums1 和 nums2.
     * <p>
     * nums1[i] = [idi, vali] 表示编号为 idi 的数字对应的值等于 vali 。
     * nums2[i] = [idi, vali] 表示编号为 idi 的数字对应的值等于 vali 。
     * 每个数组都包含 互不相同 的 id ，并按 id 以 递增 顺序排列。
     * <p>
     * 请你将两个数组合并为一个按 id 以递增顺序排列的数组，并符合下述条件：
     * <p>
     * 只有在两个数组中至少出现过一次的 id 才能包含在结果数组内。
     * 每个 id 在结果数组中 只能出现一次 ，并且其对应的值等于两个数组中该 id 所对应的值求和。如果某个数组中不存在该 id ，则认为其对应的值等于 0 。
     * 返回结果数组。返回的数组需要按 id 以递增顺序排列。
     * <p>
     * <p>
     * <p>
     * 示例 1：
     * <p>
     * 输入：nums1 = [[1,2],[2,3],[4,5]], nums2 = [[1,4],[3,2],[4,1]]
     * 输出：[[1,6],[2,3],[3,2],[4,6]]
     * 解释：结果数组中包含以下元素：
     * - id = 1 ，对应的值等于 2 + 4 = 6 。
     * - id = 2 ，对应的值等于 3 。
     * - id = 3 ，对应的值等于 2 。
     * - id = 4 ，对应的值等于5 + 1 = 6 。
     * 示例 2：
     * <p>
     * 输入：nums1 = [[2,4],[3,6],[5,5]], nums2 = [[1,3],[4,3]]
     * 输出：[[1,3],[2,4],[3,6],[4,3],[5,5]]
     * 解释：不存在共同 id ，在结果数组中只需要包含每个 id 和其对应的值。
     */
    static int[][] _2570(int[][] nums1, int[][] nums2) {
        Map<Integer, Integer> a = arrayAsMap(nums1);
        Map<Integer, Integer> b = arrayAsMap(nums2);
        Set<Integer> allIndex = new TreeSet<>(a.keySet());
        allIndex.addAll(b.keySet());
        Map<Integer, Integer> res = new LinkedHashMap<>();
        for (Integer index : allIndex) {
            res.put(index, a.getOrDefault(index, 0) + b.getOrDefault(index, 0));
        }
        int[][] resArray = new int[res.size()][2];
        int index = 0;
        for (Integer i : res.keySet()) {
            resArray[index][0] = i;
            resArray[index][1] = res.get(i);
            index++;
        }
        return resArray;
    }

    static Map<Integer, Integer> arrayAsMap(int[][] ints) {
        Map<Integer, Integer> res = new HashMap<>();
        for (int[] a : ints) {
            res.put(a[0], a[1]);
        }
        return res;
    }


    // 解法二：
    // 相当于把 nums1 与 nums2 中的元素放在各自指定索引中
    public static int[][] mergeArrays(int[][] nums1, int[][] nums2) {
        int[] temp = new int[1001];
        int res = 0;
        for (int i = 0; i < nums1.length; i++) {
            if (temp[nums1[i][0]] == 0) {
                res++;
            }
            temp[nums1[i][0]] = temp[nums1[i][0]] + nums1[i][1];
        }
        for (int i = 0; i < nums2.length; i++) {
            if (temp[nums2[i][0]] == 0) res++;
            temp[nums2[i][0]] = temp[nums2[i][0]] + nums2[i][1];
        }
        int[][] ans = new int[res][2];
        int j = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != 0) {
                ans[j][0] = i;
                ans[j][1] = temp[i];
                j++;
            }
        }
        return ans;
    }

}
