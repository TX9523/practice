public class BSNearLeft {
    public static int nearestIndex(int[] arr,int value) {
        int L=0;
        int R=arr.length-1;
        int index=-1;//记录大于等于value的最左下标
        while (L<=R) {
            int mid=L+(R-L)>>1;
            if(arr[mid]>=value) {
                index=mid;
                R=mid-1;
            }else {
                L=mid+1;
            }
        }
        return index;
    }
}
