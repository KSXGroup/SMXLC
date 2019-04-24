global main
extern printf, scanf, malloc, strlen, strcmp, sscanf, puts

SECTION .text
__user_lohi:
		push	rbp
		push	rbx
		push	r12
		push	r13
		push	r14
		mov		rbp, rsp
		sub		rsp, 56
		and		rsp, -0x10
		mov		r14, rdi
		mov		r13, rsi
lohi0:
				
		mov		rax, r13
		mov		rcx, 16
		sal		rax, cl
		mov		rbx, rax
		mov		r12, r14
		or		r12, rbx
		mov		rax, r12

		mov		rsp, rbp
		pop		r14
		pop		r13
		pop		r12
		pop		rbx
		pop		rbp
		ret



__user_toStringHex:
		push	rbp
		push	rbx
		push	r12
		push	r13
		push	r14
		push	r15
		mov		rbp, rsp
		sub		rsp, 104
		and		rsp, -0x10
		mov		qword [rbp - 16], rdi
toStringHex0:
				
		mov		rbx, __str_literal_6

toStringHex_loop_start_cond0:
				
		mov		r12, 28

toStringHex_loop_begin0:
				
		mov		rax, qword [rbp - 16]
		mov		rcx, r12
		sar		rax, cl
		mov		r15, rax
		mov		r13, r15
		and		r13, 15
		cmp		r13, 10
		jl		toStringHex_if_then0

toStringHex_if_else0:
				
		mov		r14, 65
		add		r14, r13
		mov		rax, r14
		sub		rax, 10
		mov		qword [rbp - 88], rax
		mov		rdi, qword [rbp - 88]
		call	__user_int2chr
		mov		qword [rbp - 64], rax
		mov		rdi, rbx
		mov		rsi, qword [rbp - 64]
		call	__lib_string_add
		mov		rbx, rax
		jmp		toStringHex_loop_cond0

toStringHex_if_then0:
				
		mov		rax, 48
		add		rax, r13
		mov		qword [rbp - 80], rax
		mov		rdi, qword [rbp - 80]
		call	__user_int2chr
		mov		qword [rbp - 72], rax
		mov		rdi, rbx
		mov		rsi, qword [rbp - 72]
		call	__lib_string_add
		mov		rbx, rax

toStringHex_loop_cond0:
				
		sub		r12, 4
		cmp		r12, 0
		jge		toStringHex_loop_begin0

toStringHex_loop_end0:
				
		mov		rax, rbx

		mov		rsp, rbp
		pop		r15
		pop		r14
		pop		r13
		pop		r12
		pop		rbx
		pop		rbp
		ret



__user_rotate_left:
		push	rbp
		push	rbx
		push	r12
		push	r13
		push	r14
		push	r15
		mov		rbp, rsp
		sub		rsp, 144
		and		rsp, -0x10
		mov		qword [rbp - 16], rdi
		mov		rbx, rsi
rotate_left0:
				
		jmp		rotate_left1

rotate_left2:
				
		mov		rax, 32
		sub		rax, rbx
		mov		qword [rbp - 104], rax
		mov		rax, 1
		mov		rcx, qword [rbp - 104]
		sal		rax, cl
		mov		qword [rbp - 96], rax
		sub		rax, 1
		mov		qword [rbp - 128], rax
		mov		rsi, qword [rbp - 16]
		mov		rax, rsi
		and		rax, qword [rbp - 128]
		mov		qword [rbp - 64], rax
		mov		rcx, rbx
		sal		rax, cl
		mov		qword [rbp - 80], rax
		mov		rax, 32
		sub		rax, rbx
		mov		qword [rbp - 88], rax
		mov		rsi, qword [rbp - 16]
		mov		rax, rsi
		mov		rcx, qword [rbp - 88]
		sar		rax, cl
		mov		r14, rax
		mov		rax, 1
		mov		rcx, rbx
		sal		rax, cl
		mov		qword [rbp - 120], rax
		sub		rax, 1
		mov		qword [rbp - 72], rax
		mov		rax, r14
		and		rax, qword [rbp - 72]
		mov		qword [rbp - 112], rax
		mov		rsi, qword [rbp - 80]
		mov		r12, rsi
		or		r12, qword [rbp - 112]
		mov		rdi, r12
		call	__lib_toString
		mov		r13, rax
		mov		rdi, r13
		lea		rsi, [rel __str_literal_7]
		call	__lib_string_add
		mov		r15, rax
		lea		rdi, [rel __str_literal_1]
		mov		rsi, r15
		call	printf
		mov		rax, r12

		mov		rsp, rbp
		pop		r15
		pop		r14
		pop		r13
		pop		r12
		pop		rbx
		pop		rbp
		ret



__user_add:
		push	rbp
		push	rbx
		push	r12
		push	r13
		push	r14
		push	r15
		mov		rbp, rsp
		sub		rsp, 152
		and		rsp, -0x10
		mov		qword [rbp - 16], rdi
		mov		qword [rbp - 24], rsi
add0:
				
		mov		r15, qword [rbp - 16]
		and		r15, 65535
		mov		r13, qword [rbp - 24]
		and		r13, 65535
		mov		rbx, r15
		add		rbx, r13
		mov		rax, qword [rbp - 16]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 120], rax
		and		rax, 65535
		mov		qword [rbp - 112], rax
		mov		rax, qword [rbp - 24]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 80], rax
		and		rax, 65535
		mov		qword [rbp - 104], rax
		mov		rsi, qword [rbp - 112]
		mov		rax, rsi
		add		rax, qword [rbp - 104]
		mov		qword [rbp - 88], rax
		mov		rax, rbx
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 96], rax
		mov		rsi, qword [rbp - 88]
		mov		rax, rsi
		add		rax, qword [rbp - 96]
		mov		qword [rbp - 128], rax
		and		rax, 65535
		mov		qword [rbp - 40], rax
		mov		rcx, 16
		sal		rax, cl
		mov		r12, rax
		mov		r14, rbx
		and		r14, 65535
		mov		rax, r12
		or		rax, r14
		mov		qword [rbp - 136], rax

		mov		rsp, rbp
		pop		r15
		pop		r14
		pop		r13
		pop		r12
		pop		rbx
		pop		rbp
		ret



__user_int2chr:
		push	rbp
		push	rbx
		push	r12
		push	r13
		push	r14
		push	r15
		mov		rbp, rsp
		sub		rsp, 64
		and		rsp, -0x10
		mov		rbx, rdi
int2chr0:
				
		mov		r12, 0
		cmp		rbx, 32
		jl		skip_next0

AND_test_more0:
				
		cmp		rbx, 126
		jg		skip_next0

AND_set_val0:
				
		mov		r12, 1

skip_next0:
				
		cmp		r12, 0
		je		int2chr1

int2chr_if_then0:
				
		mov		r15, rbx
		sub		r15, 32
		mov		r13, rbx
		sub		r13, 32
		mov		rdi, r15
		mov		rsi, r13
		lea		rdx, [rel __str_literal_5]
		call	__lib_str_substring
		mov		r14, rax

		mov		rsp, rbp
		pop		r15
		pop		r14
		pop		r13
		pop		r12
		pop		rbx
		pop		rbp
		ret

int2chr1:
				
		mov		rax, __str_literal_6

		mov		rsp, rbp
		pop		r15
		pop		r14
		pop		r13
		pop		r12
		pop		rbx
		pop		rbp
		ret



__user_sha1:
		push	rbp
		push	rbx
		push	r12
		push	r13
		push	r14
		push	r15
		mov		rbp, rsp
		sub		rsp, 1504
		and		rsp, -0x10
		mov		qword [rbp - 16], rdi
		mov		r14, rsi
sha10:
				
		mov		rax, r14
		add		rax, 64
		mov		qword [rbp - 664], rax
		sub		rax, 56
		mov		qword [rbp - 1464], rax
		mov		rdi, 64
		mov		rax, qword [rbp - 1464]
		cqo
		idiv	rdi
		mov		qword [rbp - 176], rax
		mov		r13, qword [rbp - 176]
		add		r13, 1
		cmp		r13, qword [rel _user_global_MAXCHUNK]
		jle		sha1_loop_start_cond0

sha1_if_then0:
				
		lea		rdi, [rel __str_literal_0]
		lea		rsi, [rel __str_literal_8]
		call	printf
		mov		rax, 0

		mov		rsp, rbp
		pop		r15
		pop		r14
		pop		r13
		pop		r12
		pop		rbx
		pop		rbp
		ret

sha1_loop_start_cond0:
				
		mov		rbx, 0
		cmp		r13, 0
		jle		sha1_loop_start_cond2

sha1_loop_start_cond1:
				
		mov		r12, 0

sha1_loop_begin1:
				
		mov		rcx, qword [rel _user_global_chunks]
		mov		rdi, qword [rcx + rbx * 8 + 8]
		mov		qword [rbp - 976], rdi
		mov		rcx, qword [rbp - 976]
		mov		qword [rcx + r12 * 8 + 8], 0

sha1_loop_cond1:
				
		add		r12, 1
		cmp		r12, 80
		jl		sha1_loop_begin1

sha1_loop_cond0:
				
		add		rbx, 1
		cmp		rbx, r13
		jl		sha1_loop_start_cond1

sha1_loop_start_cond2:
				
		mov		rbx, 0
		cmp		r14, 0
		jle		sha1_loop_end2

sha1_loop_begin2:
				
		mov		rdi, 64
		mov		rax, rbx
		cqo
		idiv	rdi
		mov		qword [rbp - 1432], rax
		mov		rcx, qword [rel _user_global_chunks]
		mov		rdi, qword [rbp - 1432]
		mov		rdi, qword [rcx + rdi * 8 + 8]
		mov		qword [rbp - 256], rdi
		mov		rdi, 64
		mov		rax, rbx
		cqo
		idiv	rdi
		mov		qword [rbp - 552], rdx
		mov		rdi, 4
		mov		rax, qword [rbp - 552]
		cqo
		idiv	rdi
		mov		qword [rbp - 864], rax
		mov		rdi, 64
		mov		rax, rbx
		cqo
		idiv	rdi
		mov		qword [rbp - 1048], rax
		mov		rcx, qword [rel _user_global_chunks]
		mov		rdi, qword [rbp - 1048]
		mov		rdi, qword [rcx + rdi * 8 + 8]
		mov		qword [rbp - 184], rdi
		mov		rdi, 64
		mov		rax, rbx
		cqo
		idiv	rdi
		mov		qword [rbp - 680], rdx
		mov		rdi, 4
		mov		rax, qword [rbp - 680]
		cqo
		idiv	rdi
		mov		qword [rbp - 600], rax
		mov		rdi, 4
		mov		rax, rbx
		cqo
		idiv	rdi
		mov		qword [rbp - 520], rdx
		mov		rax, 3
		sub		rax, qword [rbp - 520]
		mov		qword [rbp - 1288], rax
		imul	rax, 8
		mov		qword [rbp - 936], rax
		mov		rcx, qword [rbp - 16]
		mov		rsi, qword [rcx + rbx * 8 + 8]
		mov		rax, rsi
		mov		rcx, qword [rbp - 936]
		sal		rax, cl
		mov		qword [rbp - 896], rax
		mov		rcx, qword [rbp - 184]
		mov		rsi, qword [rbp - 600]
		mov		rsi, qword [rcx + rsi * 8 + 8]
		mov		rax, rsi
		or		rax, qword [rbp - 896]
		mov		qword [rbp - 648], rax
		mov		rcx, qword [rbp - 256]
		mov		rsi, qword [rbp - 864]
		mov		rdi, qword [rbp - 648]
		mov		qword [rcx + rsi * 8 + 8], rdi

sha1_loop_cond2:
				
		add		rbx, 1
		cmp		rbx, r14
		jl		sha1_loop_begin2

sha1_loop_end2:
				
		mov		rdi, 64
		mov		rax, rbx
		cqo
		idiv	rdi
		mov		qword [rbp - 360], rax
		mov		rcx, qword [rel _user_global_chunks]
		mov		rdi, qword [rbp - 360]
		mov		rdi, qword [rcx + rdi * 8 + 8]
		mov		qword [rbp - 424], rdi
		mov		rdi, 64
		mov		rax, rbx
		cqo
		idiv	rdi
		mov		qword [rbp - 1096], rdx
		mov		rdi, 4
		mov		rax, qword [rbp - 1096]
		cqo
		idiv	rdi
		mov		qword [rbp - 872], rax
		mov		rdi, 64
		mov		rax, rbx
		cqo
		idiv	rdi
		mov		qword [rbp - 848], rax
		mov		rcx, qword [rel _user_global_chunks]
		mov		rdi, qword [rbp - 848]
		mov		rdi, qword [rcx + rdi * 8 + 8]
		mov		qword [rbp - 216], rdi
		mov		rdi, 64
		mov		rax, rbx
		cqo
		idiv	rdi
		mov		qword [rbp - 496], rdx
		mov		rdi, 4
		mov		rax, qword [rbp - 496]
		cqo
		idiv	rdi
		mov		qword [rbp - 824], rax
		mov		rdi, 4
		mov		rax, rbx
		cqo
		idiv	rdi
		mov		qword [rbp - 920], rdx
		mov		rax, 3
		sub		rax, qword [rbp - 920]
		mov		qword [rbp - 1104], rax
		imul	rax, 8
		mov		qword [rbp - 1016], rax
		mov		rax, 128
		mov		rcx, qword [rbp - 1016]
		sal		rax, cl
		mov		qword [rbp - 1208], rax
		mov		rcx, qword [rbp - 216]
		mov		rsi, qword [rbp - 824]
		mov		rsi, qword [rcx + rsi * 8 + 8]
		mov		rax, rsi
		or		rax, qword [rbp - 1208]
		mov		qword [rbp - 1472], rax
		mov		rcx, qword [rbp - 424]
		mov		rsi, qword [rbp - 872]
		mov		rdi, qword [rbp - 1472]
		mov		qword [rcx + rsi * 8 + 8], rdi
		mov		rax, r13
		sub		rax, 1
		mov		qword [rbp - 400], rax
		mov		rcx, qword [rel _user_global_chunks]
		mov		rdi, qword [rbp - 400]
		mov		rdi, qword [rcx + rdi * 8 + 8]
		mov		qword [rbp - 1272], rdi
		mov		rax, r14
		mov		rcx, 3
		sal		rax, cl
		mov		qword [rbp - 1344], rax
		mov		rcx, qword [rbp - 1272]
		mov		rdi, qword [rbp - 1344]
		mov		qword [rcx + 128], rdi
		mov		rax, r13
		sub		rax, 1
		mov		qword [rbp - 1152], rax
		mov		rcx, qword [rel _user_global_chunks]
		mov		rdi, qword [rbp - 1152]
		mov		rdi, qword [rcx + rdi * 8 + 8]
		mov		qword [rbp - 816], rdi
		mov		rax, r14
		mov		rcx, 29
		sar		rax, cl
		mov		qword [rbp - 544], rax
		and		rax, 7
		mov		qword [rbp - 320], rax
		mov		rcx, qword [rbp - 816]
		mov		rdi, qword [rbp - 320]
		mov		qword [rcx + 120], rdi
		mov		qword [rbp - 56], 1732584193
		mov		qword [rbp - 232], -271733879
		mov		rdi, qword [rbp - 232]
		mov		qword [rbp - 64], rdi

sha1_inline_next0:
				
		mov		qword [rbp - 272], -1732584194
		mov		rdi, qword [rbp - 272]
		mov		qword [rbp - 72], rdi

sha1_inline_next1:
				
		mov		qword [rbp - 80], 271733878
		mov		qword [rbp - 1080], -1009589776
		mov		rdi, qword [rbp - 1080]
		mov		qword [rbp - 88], rdi

sha1_loop_start_cond3:
				
		cmp		r13, 0
		jle		sha1_loop_end3
		jmp		sha1_inline_rotate_left10

sha1_inline_rotate_left20:
				
		mov		rax, 32
		sub		rax, qword [rbp - 1368]
		mov		qword [rbp - 536], rax
		mov		rax, 1
		mov		rcx, qword [rbp - 536]
		sal		rax, cl
		mov		qword [rbp - 1200], rax
		sub		rax, 1
		mov		qword [rbp - 1120], rax
		mov		rsi, qword [rbp - 368]
		mov		rax, rsi
		and		rax, qword [rbp - 1120]
		mov		qword [rbp - 1144], rax
		mov		rsi, qword [rbp - 1144]
		mov		rax, rsi
		mov		rcx, qword [rbp - 1368]
		sal		rax, cl
		mov		qword [rbp - 672], rax
		mov		rax, 32
		sub		rax, qword [rbp - 1368]
		mov		qword [rbp - 1264], rax
		mov		rsi, qword [rbp - 368]
		mov		rax, rsi
		mov		rcx, qword [rbp - 1264]
		sar		rax, cl
		mov		qword [rbp - 1072], rax
		mov		rax, 1
		mov		rcx, qword [rbp - 1368]
		sal		rax, cl
		mov		qword [rbp - 240], rax
		sub		rax, 1
		mov		qword [rbp - 432], rax
		mov		rsi, qword [rbp - 1072]
		mov		rax, rsi
		and		rax, qword [rbp - 432]
		mov		qword [rbp - 1280], rax
		mov		rsi, qword [rbp - 672]
		mov		rax, rsi
		or		rax, qword [rbp - 1280]
		mov		qword [rbp - 1176], rax
		mov		rdi, qword [rbp - 1176]
		call	__lib_toString
		mov		qword [rbp - 1408], rax
		mov		rdi, qword [rbp - 1408]
		lea		rsi, [rel __str_literal_7]
		call	__lib_string_add
		mov		qword [rbp - 728], rax
		lea		rdi, [rel __str_literal_1]
		mov		rsi, qword [rbp - 728]
		call	printf
		mov		rdi, qword [rbp - 1176]
		mov		qword [rbp - 376], rdi

sha1_inline_next3:
				
		mov		rcx, qword [rbp - 264]
		mov		rdi, qword [rbp - 376]
		mov		qword [rcx + r12 * 8 + 8], rdi

sha1_loop_cond4:
				
		add		r12, 1
		cmp		r12, 80
		jl		sha1_inline_rotate_left10

sha1_loop_start_cond5:
				
		mov		r12, 0

sha1_loop_begin5:
				
		cmp		r12, 20
		jl		sha1_inline_rotate_left11

sha1_if_else1:
				
		cmp		r12, 40
		jl		sha1_inline_rotate_left11
		jmp		sha1_if_else2

sha1_inline_rotate_left21:
				
		mov		rax, 32
		sub		rax, r15
		mov		qword [rbp - 840], rax
		mov		rax, 1
		mov		rcx, qword [rbp - 840]
		sal		rax, cl
		mov		qword [rbp - 752], rax
		sub		rax, 1
		mov		qword [rbp - 640], rax
		mov		rsi, qword [rbp - 1000]
		mov		rax, rsi
		and		rax, qword [rbp - 640]
		mov		qword [rbp - 1216], rax
		mov		rcx, r15
		sal		rax, cl
		mov		qword [rbp - 960], rax
		mov		rax, 32
		sub		rax, r15
		mov		qword [rbp - 1448], rax
		mov		rsi, qword [rbp - 1000]
		mov		rax, rsi
		mov		rcx, qword [rbp - 1448]
		sar		rax, cl
		mov		qword [rbp - 1112], rax
		mov		rax, 1
		mov		rcx, r15
		sal		rax, cl
		mov		qword [rbp - 576], rax
		sub		rax, 1
		mov		qword [rbp - 504], rax
		mov		rsi, qword [rbp - 1112]
		mov		rax, rsi
		and		rax, qword [rbp - 504]
		mov		qword [rbp - 1184], rax
		mov		rsi, qword [rbp - 960]
		mov		rax, rsi
		or		rax, qword [rbp - 1184]
		mov		qword [rbp - 384], rax
		mov		rdi, qword [rbp - 384]
		call	__lib_toString
		mov		qword [rbp - 1392], rax
		mov		rdi, qword [rbp - 1392]
		lea		rsi, [rel __str_literal_7]
		call	__lib_string_add
		mov		qword [rbp - 1424], rax
		lea		rdi, [rel __str_literal_1]
		mov		rsi, qword [rbp - 1424]
		call	printf
		jmp		sha1_inline_rotate_left12

sha1_inline_rotate_left22:
				
		mov		rax, 32
		sub		rax, qword [rbp - 1416]
		mov		qword [rbp - 328], rax
		mov		rax, 1
		mov		rcx, qword [rbp - 328]
		sal		rax, cl
		mov		qword [rbp - 584], rax
		sub		rax, 1
		mov		qword [rbp - 624], rax
		mov		rsi, qword [rbp - 448]
		mov		rax, rsi
		and		rax, qword [rbp - 624]
		mov		qword [rbp - 1240], rax
		mov		rsi, qword [rbp - 1240]
		mov		rax, rsi
		mov		rcx, qword [rbp - 1416]
		sal		rax, cl
		mov		qword [rbp - 560], rax
		mov		rax, 32
		sub		rax, qword [rbp - 1416]
		mov		qword [rbp - 992], rax
		mov		rsi, qword [rbp - 448]
		mov		rax, rsi
		mov		rcx, qword [rbp - 992]
		sar		rax, cl
		mov		qword [rbp - 952], rax
		mov		rax, 1
		mov		rcx, qword [rbp - 1416]
		sal		rax, cl
		mov		qword [rbp - 280], rax
		sub		rax, 1
		mov		qword [rbp - 1160], rax
		mov		rsi, qword [rbp - 952]
		mov		rax, rsi
		and		rax, qword [rbp - 1160]
		mov		qword [rbp - 1480], rax
		mov		rsi, qword [rbp - 560]
		mov		rax, rsi
		or		rax, qword [rbp - 1480]
		mov		qword [rbp - 1248], rax
		mov		rdi, qword [rbp - 1248]
		call	__lib_toString
		mov		qword [rbp - 160], rax
		mov		rdi, qword [rbp - 160]
		lea		rsi, [rel __str_literal_7]
		call	__lib_string_add
		mov		qword [rbp - 696], rax
		lea		rdi, [rel __str_literal_1]
		mov		rsi, qword [rbp - 696]
		call	printf
		mov		rdi, qword [rbp - 1248]
		mov		qword [rbp - 112], rdi

sha1_inline_next11:
				
		mov		rdi, qword [rbp - 96]
		mov		qword [rbp - 104], rdi
		mov		rdi, qword [rbp - 152]
		mov		qword [rbp - 96], rdi

sha1_loop_cond5:
				
		add		r12, 1
		cmp		r12, 80
		jl		sha1_loop_begin5

sha1_loop_end5:
				
		mov		rdi, qword [rbp - 56]
		mov		qword [rbp - 336], rdi
		mov		rdi, qword [rbp - 96]
		mov		qword [rbp - 528], rdi
		mov		rax, qword [rbp - 336]
		and		rax, 65535
		mov		qword [rbp - 480], rax
		mov		rax, qword [rbp - 528]
		and		rax, 65535
		mov		qword [rbp - 312], rax
		mov		rsi, qword [rbp - 480]
		mov		rax, rsi
		add		rax, qword [rbp - 312]
		mov		qword [rbp - 592], rax
		mov		rax, qword [rbp - 336]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 1008], rax
		and		rax, 65535
		mov		qword [rbp - 928], rax
		mov		rax, qword [rbp - 528]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 1320], rax
		and		rax, 65535
		mov		qword [rbp - 712], rax
		mov		rsi, qword [rbp - 928]
		mov		rax, rsi
		add		rax, qword [rbp - 712]
		mov		qword [rbp - 880], rax
		mov		rax, qword [rbp - 592]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 1336], rax
		mov		rsi, qword [rbp - 880]
		mov		rax, rsi
		add		rax, qword [rbp - 1336]
		mov		qword [rbp - 1304], rax
		and		rax, 65535
		mov		qword [rbp - 1032], rax
		mov		rcx, 16
		sal		rax, cl
		mov		qword [rbp - 1064], rax
		mov		rax, qword [rbp - 592]
		and		rax, 65535
		mov		qword [rbp - 776], rax
		mov		rsi, qword [rbp - 1064]
		mov		rax, rsi
		or		rax, qword [rbp - 776]
		mov		qword [rbp - 792], rax
		mov		rdi, qword [rbp - 792]
		mov		qword [rbp - 56], rdi

sha1_inline_next12:
				
		mov		rdi, qword [rbp - 64]
		mov		qword [rbp - 1024], rdi
		mov		rdi, qword [rbp - 104]
		mov		qword [rbp - 1376], rdi
		mov		rax, qword [rbp - 1024]
		and		rax, 65535
		mov		qword [rbp - 688], rax
		mov		rax, qword [rbp - 1376]
		and		rax, 65535
		mov		qword [rbp - 1296], rax
		mov		rsi, qword [rbp - 688]
		mov		rax, rsi
		add		rax, qword [rbp - 1296]
		mov		qword [rbp - 512], rax
		mov		rax, qword [rbp - 1024]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 1128], rax
		and		rax, 65535
		mov		qword [rbp - 720], rax
		mov		rax, qword [rbp - 1376]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 1384], rax
		and		rax, 65535
		mov		qword [rbp - 248], rax
		mov		rsi, qword [rbp - 720]
		mov		rax, rsi
		add		rax, qword [rbp - 248]
		mov		qword [rbp - 944], rax
		mov		rax, qword [rbp - 512]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 224], rax
		mov		rsi, qword [rbp - 944]
		mov		rax, rsi
		add		rax, qword [rbp - 224]
		mov		qword [rbp - 1488], rax
		and		rax, 65535
		mov		qword [rbp - 392], rax
		mov		rcx, 16
		sal		rax, cl
		mov		qword [rbp - 832], rax
		mov		rax, qword [rbp - 512]
		and		rax, 65535
		mov		qword [rbp - 656], rax
		mov		rsi, qword [rbp - 832]
		mov		rax, rsi
		or		rax, qword [rbp - 656]
		mov		qword [rbp - 200], rax
		mov		rdi, qword [rbp - 200]
		mov		qword [rbp - 64], rdi

sha1_inline_next13:
				
		mov		rdi, qword [rbp - 72]
		mov		qword [rbp - 800], rdi
		mov		rdi, qword [rbp - 112]
		mov		qword [rbp - 1224], rdi
		mov		rax, qword [rbp - 800]
		and		rax, 65535
		mov		qword [rbp - 784], rax
		mov		rax, qword [rbp - 1224]
		and		rax, 65535
		mov		qword [rbp - 1056], rax
		mov		rsi, qword [rbp - 784]
		mov		rax, rsi
		add		rax, qword [rbp - 1056]
		mov		qword [rbp - 344], rax
		mov		rax, qword [rbp - 800]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 632], rax
		and		rax, 65535
		mov		qword [rbp - 912], rax
		mov		rax, qword [rbp - 1224]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 704], rax
		and		rax, 65535
		mov		qword [rbp - 616], rax
		mov		rsi, qword [rbp - 912]
		mov		rax, rsi
		add		rax, qword [rbp - 616]
		mov		qword [rbp - 464], rax
		mov		rax, qword [rbp - 344]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 296], rax
		mov		rsi, qword [rbp - 464]
		mov		rax, rsi
		add		rax, qword [rbp - 296]
		mov		qword [rbp - 1232], rax
		and		rax, 65535
		mov		qword [rbp - 408], rax
		mov		rcx, 16
		sal		rax, cl
		mov		qword [rbp - 1360], rax
		mov		rax, qword [rbp - 344]
		and		rax, 65535
		mov		qword [rbp - 288], rax
		mov		rsi, qword [rbp - 1360]
		mov		rax, rsi
		or		rax, qword [rbp - 288]
		mov		qword [rbp - 1352], rax
		mov		rdi, qword [rbp - 1352]
		mov		qword [rbp - 72], rdi

sha1_inline_next14:
				
		mov		rdi, qword [rbp - 80]
		mov		qword [rbp - 760], rdi
		mov		rdi, qword [rbp - 120]
		mov		qword [rbp - 608], rdi
		mov		rax, qword [rbp - 760]
		and		rax, 65535
		mov		qword [rbp - 768], rax
		mov		rax, qword [rbp - 608]
		and		rax, 65535
		mov		qword [rbp - 488], rax
		mov		rsi, qword [rbp - 768]
		mov		rax, rsi
		add		rax, qword [rbp - 488]
		mov		qword [rbp - 808], rax
		mov		rax, qword [rbp - 760]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 1168], rax
		and		rax, 65535
		mov		qword [rbp - 192], rax
		mov		rax, qword [rbp - 608]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 904], rax
		and		rax, 65535
		mov		qword [rbp - 984], rax
		mov		rsi, qword [rbp - 192]
		mov		rax, rsi
		add		rax, qword [rbp - 984]
		mov		qword [rbp - 352], rax
		mov		rax, qword [rbp - 808]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 1192], rax
		mov		rsi, qword [rbp - 352]
		mov		rax, rsi
		add		rax, qword [rbp - 1192]
		mov		qword [rbp - 304], rax
		and		rax, 65535
		mov		qword [rbp - 856], rax
		mov		rcx, 16
		sal		rax, cl
		mov		qword [rbp - 888], rax
		mov		rax, qword [rbp - 808]
		and		rax, 65535
		mov		qword [rbp - 168], rax
		mov		rsi, qword [rbp - 888]
		mov		rax, rsi
		or		rax, qword [rbp - 168]
		mov		qword [rbp - 472], rax
		mov		rdi, qword [rbp - 472]
		mov		qword [rbp - 80], rdi

sha1_inline_next15:
				
		mov		rdi, qword [rbp - 88]
		mov		qword [rbp - 736], rdi
		mov		rdi, qword [rbp - 128]
		mov		qword [rbp - 1440], rdi
		mov		rax, qword [rbp - 736]
		and		rax, 65535
		mov		qword [rbp - 1312], rax
		mov		rax, qword [rbp - 1440]
		and		rax, 65535
		mov		qword [rbp - 1136], rax
		mov		rsi, qword [rbp - 1312]
		mov		rax, rsi
		add		rax, qword [rbp - 1136]
		mov		qword [rbp - 1400], rax
		mov		rax, qword [rbp - 736]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 456], rax
		and		rax, 65535
		mov		qword [rbp - 744], rax
		mov		rax, qword [rbp - 1440]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 1088], rax
		and		rax, 65535
		mov		qword [rbp - 1328], rax
		mov		rsi, qword [rbp - 744]
		mov		rax, rsi
		add		rax, qword [rbp - 1328]
		mov		qword [rbp - 1456], rax
		mov		rax, qword [rbp - 1400]
		mov		rcx, 16
		sar		rax, cl
		mov		qword [rbp - 440], rax
		mov		rsi, qword [rbp - 1456]
		mov		rax, rsi
		add		rax, qword [rbp - 440]
		mov		qword [rbp - 968], rax
		and		rax, 65535
		mov		qword [rbp - 1040], rax
		mov		rcx, 16
		sal		rax, cl
		mov		qword [rbp - 568], rax
		mov		rax, qword [rbp - 1400]
		and		rax, 65535
		mov		qword [rbp - 1256], rax
		mov		rsi, qword [rbp - 568]
		mov		rax, rsi
		or		rax, qword [rbp - 1256]
		mov		qword [rbp - 416], rax
		mov		rdi, qword [rbp - 416]
		mov		qword [rbp - 88], rdi

sha1_loop_cond3:
				
		add		rbx, 1
		cmp		rbx, r13
		jl		sha1_inline_rotate_left10

sha1_loop_end3:
				
		mov		rcx, qword [rel _user_global_outputBuffer]
		mov		rdi, qword [rbp - 56]
		mov		qword [rcx + 8], rdi
		mov		rcx, qword [rel _user_global_outputBuffer]
		mov		rdi, qword [rbp - 64]
		mov		qword [rcx + 16], rdi
		mov		rcx, qword [rel _user_global_outputBuffer]
		mov		rdi, qword [rbp - 72]
		mov		qword [rcx + 24], rdi
		mov		rcx, qword [rel _user_global_outputBuffer]
		mov		rdi, qword [rbp - 80]
		mov		qword [rcx + 32], rdi
		mov		rcx, qword [rel _user_global_outputBuffer]
		mov		rdi, qword [rbp - 88]
		mov		qword [rcx + 40], rdi
		mov		rax, qword [rel _user_global_outputBuffer]

		mov		rsp, rbp
		pop		r15
		pop		r14
		pop		r13
		pop		r12
		pop		rbx
		pop		rbp
		ret



__user_computeSHA1:
		push	rbp
		push	rbx
		push	r12
		push	r13
		push	r14
		push	r15
		mov		rbp, rsp
		sub		rsp, 112
		and		rsp, -0x10
		mov		r13, rdi
computeSHA10:
				

computeSHA1_loop_start_cond0:
				
		mov		rbx, 0
		mov		rdi, r13
		call	strlen
		mov		qword [rbp - 88], rax
		cmp		qword [rbp - 88], 0
		jle		computeSHA1_loop_end0

computeSHA1_loop_begin0:
				
		mov		rdi, rbx
		mov		rsi, r13
		movsx	rax, byte [rsi + rdi]
		mov		qword [rbp - 80], rax
		mov		rcx, qword [rel _user_global_inputBuffer]
		mov		rdi, qword [rbp - 80]
		mov		qword [rcx + rbx * 8 + 8], rdi

computeSHA1_loop_cond0:
				
		add		rbx, 1
		mov		rdi, r13
		call	strlen
		mov		qword [rbp - 96], rax
		cmp		rbx, qword [rbp - 96]
		jl		computeSHA1_loop_begin0

computeSHA1_loop_end0:
				
		mov		rdi, r13
		call	strlen
		mov		qword [rbp - 64], rax
		mov		rdi, qword [rel _user_global_inputBuffer]
		mov		rsi, qword [rbp - 64]
		call	__user_sha1
		mov		qword [rbp - 56], rax
		mov		r12, qword [rbp - 56]

computeSHA1_loop_start_cond1:
				
		mov		rbx, 0
		mov		rdi, qword [r12]
		mov		qword [rbp - 72], rdi
		cmp		qword [rbp - 72], 0
		jle		computeSHA1_loop_end1

computeSHA1_loop_begin1:
				
		mov		r15, qword [r12 + rbx * 8 + 8]
		lea		rdi, [rel __str_literal_2]
		mov		rsi, r15
		call	printf

computeSHA1_loop_cond1:
				
		add		rbx, 1
		mov		r14, qword [r12]
		cmp		rbx, r14
		jl		computeSHA1_loop_begin1

computeSHA1_loop_end1:
				
		lea		rdi, [rel __str_literal_0]
		lea		rsi, [rel __str_literal_6]
		call	printf

		mov		rsp, rbp
		pop		r15
		pop		r14
		pop		r13
		pop		r12
		pop		rbx
		pop		rbp
		ret



main:
		push	rbp
		mov		rbp, rsp
		sub		rsp, 216
		and		rsp, -0x10
global0:
				
		mov		qword [rel _user_global_asciiTable], __str_literal_5
		mov		rax, qword [rel _user_global_MAXCHUNK]
		sub		rax, 1
		mov		qword [rbp - 176], rax
		imul	rax, 64
		mov		qword [rbp - 88], rax
		sub		rax, 16
		mov		qword [rel _user_global_MAXLENGTH], rax
		mov		rax, qword [rel _user_global_MAXCHUNK]
		imul	rax, 8
		mov		qword [rbp - 56], rax
		add		rax, 8
		mov		qword [rbp - 80], rax
		mov		rdi, qword [rbp - 80]
		call	malloc
		mov		r14, rax
		mov		rdi, qword [rel _user_global_MAXCHUNK]
		mov		qword [r14], rdi
		mov		r13, 0

creator_loop0:
				
		mov		rdi, 648
		call	malloc
		mov		qword [rbp - 40], rax
		mov		rcx, qword [rbp - 40]
		mov		qword [rcx], 80
		mov		rdi, qword [rbp - 40]
		mov		qword [r14 + r13 * 8 + 8], rdi
		add		r13, 1
		cmp		r13, qword [rel _user_global_MAXCHUNK]
		jl		creator_loop0

creator_loop_end0:
				
		mov		qword [rel _user_global_chunks], r14
		mov		rax, qword [rel _user_global_MAXLENGTH]
		imul	rax, 8
		mov		qword [rbp - 192], rax
		add		rax, 8
		mov		qword [rbp - 48], rax
		mov		rdi, qword [rbp - 48]
		call	malloc
		mov		qword [rbp - 128], rax
		mov		rcx, qword [rbp - 128]
		mov		rdi, qword [rel _user_global_MAXLENGTH]
		mov		qword [rcx], rdi
		mov		rdi, qword [rbp - 128]
		mov		qword [rel _user_global_inputBuffer], rdi
		mov		rdi, 48
		call	malloc
		mov		qword [rbp - 136], rax
		mov		rcx, qword [rbp - 136]
		mov		qword [rcx], 5
		mov		rdi, qword [rbp - 136]
		mov		qword [rel _user_global_outputBuffer], rdi

main0:
				
		call	__lib_getString
		mov		qword [rbp - 24], rax
		mov		r12, qword [rbp - 24]

main_inline_computeSHA1_loop_start_cond00:
				
		mov		rbx, 0
		mov		rdi, r12
		call	strlen
		mov		qword [rbp - 104], rax
		cmp		qword [rbp - 104], 0
		jle		main_inline_computeSHA1_loop_end00

main_inline_computeSHA1_loop_begin00:
				
		mov		rdi, rbx
		mov		rsi, r12
		movsx	rax, byte [rsi + rdi]
		mov		qword [rbp - 120], rax
		mov		rcx, qword [rel _user_global_inputBuffer]
		mov		rdi, qword [rbp - 120]
		mov		qword [rcx + rbx * 8 + 8], rdi

main_inline_computeSHA1_loop_cond00:
				
		add		rbx, 1
		mov		rdi, r12
		call	strlen
		mov		qword [rbp - 96], rax
		cmp		rbx, qword [rbp - 96]
		jl		main_inline_computeSHA1_loop_begin00

main_inline_computeSHA1_loop_end00:
				
		mov		rdi, r12
		call	strlen
		mov		qword [rbp - 160], rax
		mov		rdi, qword [rel _user_global_inputBuffer]
		mov		rsi, qword [rbp - 160]
		call	__user_sha1
		mov		qword [rbp - 112], rax
		mov		r15, qword [rbp - 112]

main_inline_computeSHA1_loop_start_cond10:
				
		mov		rbx, 0
		mov		rdi, qword [r15]
		mov		qword [rbp - 184], rdi
		cmp		qword [rbp - 184], 0
		jle		main_inline_computeSHA1_loop_end10

main_inline_computeSHA1_loop_begin10:
				
		mov		rdi, qword [r15 + rbx * 8 + 8]
		mov		qword [rbp - 64], rdi
		lea		rdi, [rel __str_literal_2]
		mov		rsi, qword [rbp - 64]
		call	printf

main_inline_computeSHA1_loop_cond10:
				
		add		rbx, 1
		mov		rdi, qword [r15]
		mov		qword [rbp - 144], rdi
		cmp		rbx, qword [rbp - 144]
		jl		main_inline_computeSHA1_loop_begin10

main_inline_computeSHA1_loop_end10:
				
		lea		rdi, [rel __str_literal_0]
		lea		rsi, [rel __str_literal_6]
		call	printf

main_inline_next0:
				
		mov		rax, 0
		leave
		ret




SECTION .data	align=8
__str_literal_6:
		db 00H
__str_literal_7:
		db 20H, 00H
__str_literal_5:
		db 20H, 21H, 22H, 23H, 24H, 25H, 26H, 27H, 28H, 29H, 2AH, 2BH, 2CH, 2DH, 2EH, 2FH, 30H, 31H, 32H, 33H, 34H, 35H, 36H, 37H, 38H, 39H, 3AH, 3BH, 3CH, 3DH, 3EH, 3FH, 40H, 41H, 42H, 43H, 44H, 45H, 46H, 47H, 48H, 49H, 4AH, 4BH, 4CH, 4DH, 4EH, 4FH, 50H, 51H, 52H, 53H, 54H, 55H, 56H, 57H, 58H, 59H, 5AH, 5BH, 5CH, 5DH, 5EH, 5FH, 60H, 61H, 62H, 63H, 64H, 65H, 66H, 67H, 68H, 69H, 6AH, 6BH, 6CH, 6DH, 6EH, 6FH, 70H, 71H, 72H, 73H, 74H, 75H, 76H, 77H, 78H, 79H, 7AH, 7BH, 7CH, 7DH, 7EH, 00H
__str_literal_8:
		db 6EH, 43H, 68H, 75H, 6EH, 6BH, 20H, 3EH, 20H, 4DH, 41H, 58H, 43H, 48H, 55H, 4EH, 4BH, 21H, 00H
__str_literal_0:
		db 25H, 73H, 0AH, 00H
__str_literal_3:
		db 25H, 6CH, 64H, 00H
__str_literal_1:
		db 25H, 73H, 00H
__str_literal_2:
		db 25H, 6CH, 64H, 0AH, 00H
_user_global_sfeekqw:
		dq 100
_user_global_MAXCHUNK:
		dq 100

SECTION .bss	align=8
_user_global_inputBuffer:
		resq 1
_user_global_outputBuffer:
		resq 1
_user_global_asciiTable:
		resq 1
_user_global_MAXLENGTH:
		resq 1
_user_global_chunks:
		resq 1






default rel

global __lib_getString
global __lib_toString
global __lib__string_add

extern memcpy
extern strlen
extern __sprintf_chk
extern scanf
extern malloc
extern strncpy


SECTION .text

__lib_getString:
        push    rbx
        mov     edi, 256
        call    malloc
        lea     rdi, [rel LC0]
        mov     rbx, rax
        mov     rsi, rax
        xor     eax, eax
        call    scanf
        mov     rax, rbx
        pop     rbx
        ret


ALIGN   16

__lib_toString:
        push    rbp
        push    rbx
        mov     rbp, rdi
        mov     edi, 50
        sub     rsp, 8
        call    malloc
        lea     rcx, [rel LC1]
        mov     rbx, rax
        mov     r8, rbp
        mov     rdi, rax
        mov     edx, 50
        mov     esi, 1
        xor     eax, eax
        call    __sprintf_chk
        add     rsp, 8
        mov     rax, rbx
        pop     rbx
        pop     rbp
        ret






ALIGN   8

__lib_string_add:
        push    r14
        push    r13
        mov     r13, rsi
        push    r12
        push    rbp
        mov     r14, rdi
        push    rbx
        call    strlen
        mov     rdi, r13
        mov     rbx, rax
        call    strlen
        lea     rdi, [rbx+rax+1H]
        mov     rbp, rax
        call    malloc
        mov     rdx, rbx
        mov     r12, rax
        mov     rsi, r14
        mov     rdi, rax
        call    memcpy
        lea     rdi, [r12+rbx]
        lea     rdx, [rbp+1H]
        mov     rsi, r13
        call    memcpy
        pop     rbx
        mov     rax, r12
        pop     rbp
        pop     r12
        pop     r13
        pop     r14
        ret

ALIGN   16

__lib_str_substring:
        push    r12
        push    rbp
        mov     r12, rdi
        push    rbx
        mov     rbx, rsi
        mov     rbp, rdx
        sub     rbx, rdi
        lea     rdi, [rbx+2H]
        call    malloc
        lea     rsi, [rbp+r12]
        lea     rdx, [rbx+1H]
        mov     rdi, rax
        call    strncpy
        mov     byte [rax+rbx+1H], 0
        pop     rbx
        pop     rbp
        pop     r12
        ret

SECTION .rodata

LC0:
        db 25H, 73H, 00H

LC1:
        db 25H, 6CH, 64H, 00H


