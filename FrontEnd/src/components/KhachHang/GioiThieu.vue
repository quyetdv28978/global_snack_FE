<script setup>
import { ref, onMounted } from 'vue';

const home = ref({
    icon: 'pi pi-home',
    to: { name: 'trang-chu' }
});
const items = ref([{ label: 'Computer' }, { label: 'Notebook' }, { label: 'Accessories' }, { label: 'Backpacks' }, { label: 'Item' }]);
import Divider from 'primevue/divider';
</script>

<template>
    <div class="card">
        <br />
        <h2 style="">About us</h2>
        <Divider />

        <div>
            <Panel header="Information">
                <p class="m-0">
                    Chào mừng đến với trang web của chúng tôi - nơi bạn có thể khám phá và thưởng thức những loại đồ ăn vặt ngon miệng và đa dạng. Chúng tôi tự hào là địa chỉ tin cậy dành cho những người yêu thích đồ ăn vặt từ khắp mọi nơi. Với sự đa
                    dạng về món ăn và chất lượng phục vụ hàng đầu, chúng tôi cam kết mang đến cho bạn những trải nghiệm ẩm thực tuyệt vời nhất. Tại trang web của chúng tôi, bạn sẽ tìm thấy một loạt các loại đồ ăn vặt phong phú từ khắp nơi trên thế
                    giới, từ những chiếc bánh ngọt mềm mịn đến những món snack thơm ngon đậm đà hương vị. Bất kỳ ai cũng có thể tìm thấy một món ăn vặt ưa thích tại đây, với sự đa dạng và phong phú của danh sách menu.
                </p>
            </Panel>
        </div>

        <div style="margin-top: 3rem">
            <h2>Question</h2>
            <Accordion :activeIndex="0">
                <AccordionTab header="Đồ ăn vặt của chúng tôi có đảm bảo an toàn thực phẩm không?">
                    <p class="m-0">Vâng, tất cả các sản phẩm đều được kiểm định và tuân thủ các quy định về an toàn thực phẩm để đảm bảo chất lượng tốt nhất cho khách hàng.</p>
                </AccordionTab>
                <AccordionTab header="Chúng tôi có cung cấp dịch vụ giao hàng tận nơi không?">
                    <p class="m-0">Chắc chắn, chúng tôi cung cấp dịch vụ giao hàng tận nơi thuận tiện đến địa chỉ của bạn. Vui lòng xem chi tiết trên trang web hoặc liên hệ trực tiếp để biết thêm thông tin.</p>
                </AccordionTab>
                <AccordionTab header="Đồ ăn vặt của chúng tôi có thích hợp cho những người ăn chay không?">
                    <p class="m-0">Có, chúng tôi cung cấp một loạt các món ăn vặt phù hợp cho những người ăn chay và có sẵn nhiều lựa chọn phong phú để bạn có thể thưởng thức.</p>
                </AccordionTab>
                <AccordionTab header="Chúng tôi có phục vụ các món ăn vặt phổ biến như bắp rang, khoai tây chiên không?">
                    <p class="m-0">Chúng tôi có một danh sách menu đa dạng với nhiều loại đồ ăn vặt hấp dẫn để bạn lựa chọn, bao gồm cả các loại snack, bánh ngọt và nhiều hơn nữa.</p>
                </AccordionTab>
                <AccordionTab header="Làm thế nào để liên hệ với chúng tôi để đặt hàng hoặc có thêm thông tin?">
                    <p class="m-0">Bạn có thể liên hệ với chúng tôi qua email tại support@domain.com hoặc gọi điện thoại tới số (+84) 123 456 789 để được hỗ trợ và biết thêm thông tin chi tiết.</p>
                </AccordionTab>
            </Accordion>
        </div>
    </div>
</template>

<style scoped>
body,
html {
    height: 100%;
    margin: 0;
    padding: 0;
}
.card {
    margin-top: 5rem;
}

p {
    font-family: 'Poppins', sans-serif;
    font-size: 18px;
    font-weight: 500;
}
</style>
